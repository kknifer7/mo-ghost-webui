package io.knifer.moghostwebui.service;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import io.knifer.moghostwebui.common.constant.*;
import io.knifer.moghostwebui.common.entity.domain.*;
import io.knifer.moghostwebui.common.entity.domain.key.ID;
import io.knifer.moghostwebui.common.entity.domain.key.MoFileVersionKey;
import io.knifer.moghostwebui.common.entity.request.MoFileQueryRequest;
import io.knifer.moghostwebui.common.entity.request.PageParams;
import io.knifer.moghostwebui.common.entity.vo.MoFileVO;
import io.knifer.moghostwebui.common.exception.MoException;
import io.knifer.moghostwebui.common.tool.file.FileAssessor;
import io.knifer.moghostwebui.common.tool.security.IPLocator;
import io.knifer.moghostwebui.common.util.RandomUtil;
import io.knifer.moghostwebui.common.util.ServletUtil;
import io.knifer.moghostwebui.config.properties.MoGhostProperties;
import io.knifer.moghostwebui.handle.security.impl.AccessKeyVerifier;
import io.knifer.moghostwebui.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Page;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 文件服务
 *
 * @author Knifer
 * @version 1.0.0
 */
@Slf4j
@Service
@DependsOn("asyncServiceExecutor")
public class MoFileService {

    private final MoFileRepository repository;

    private final VersionRepository versionRepository;

    private final MoFileVersionRepository moFileVersionRepository;

    private final SingleReleaseCDKRepository singleReleaseCDKRepository;

    private final SingleReleaseRepository singleReleaseRepository;

    private final SingleReleaseCDKASORepository singleReleaseCDKASORepository;

    private final FileAssessor fileAssessor;

    private final IPLocator ipLocator;

    private final AccessKeyVerifier accessKeyVerifier;

    private final String STORAGE_PATH;

    @Autowired
    public MoFileService(
            MoFileRepository repository,
            VersionRepository versionRepository,
            MoFileVersionRepository moFileVersionRepository,
            SingleReleaseCDKRepository singleReleaseCDKRepository,
            SingleReleaseRepository singleReleaseRepository,
            SingleReleaseCDKASORepository singleReleaseCDKASORepository,
            FileAssessor fileAssessor,
            IPLocator ipLocator,
            AccessKeyVerifier accessKeyVerifier,
            MoGhostProperties.StorageProperties props
    ){
        this.repository = repository;
        this.versionRepository = versionRepository;
        this.moFileVersionRepository = moFileVersionRepository;
        this.singleReleaseCDKRepository = singleReleaseCDKRepository;
        this.singleReleaseRepository = singleReleaseRepository;
        this.singleReleaseCDKASORepository = singleReleaseCDKASORepository;
        this.fileAssessor = fileAssessor;
        this.ipLocator = ipLocator;
        this.accessKeyVerifier = accessKeyVerifier;
        this.STORAGE_PATH = StringUtils.appendIfMissing(props.getSavePath(), "/");
    }

    /**
     * 分页查询
     * @param pageParams 分页参数
     * @param request 请求参数
     * @return 分页数据
     */
    public Page<MoFileVO> listPage(PageParams pageParams, MoFileQueryRequest request){
        Page<MoFile> result = repository.findAll(request.toExample(), pageParams.toPageRequest());
        List<MoFile> poList = result.getContent();
        Collection<MoFileVersion> asos;
        ImmutableMultimap.Builder<Integer, Integer> moFileIdVersionIdMapBuilder;
        Multimap<Integer, Integer> moFileIdVersionIdMap;
        Map<Integer, Version> versionIdMap;

        if (poList.isEmpty()){
            return Page.empty();
        }
        // 如存在，获取文件与版本号的关联关系
        asos = moFileVersionRepository.findByIdMoFileIdIn(ID.mapIds(poList));
        if (asos.isEmpty()){
            return result.map(MoFileVO::from);
        }
        moFileIdVersionIdMapBuilder = ImmutableMultimap.builder();
        asos.forEach(a -> {
            MoFileVersionKey key = a.getId();

            moFileIdVersionIdMapBuilder.put(key.getMoFileId(), key.getVersionId());
        });
        moFileIdVersionIdMap = moFileIdVersionIdMapBuilder.build();
        versionIdMap = ID.mapIdsMap(versionRepository.findAllById(moFileIdVersionIdMap.values()));

        return result.map(f -> {
            List<Version> versions = moFileIdVersionIdMap.get(f.getId())
                    .stream()
                    .map(versionIdMap::get)
                    .toList();

            return MoFileVO.of(f, versions);
        });
    }

    /**
     * 分页查询独立文件
     * @param pageParams 分页参数
     * @return 分页数据
     */
    public Page<MoFileVO> listPageSingle(PageParams pageParams){
        return repository.findByVersionIdNotExists(pageParams.toPageRequest())
                .map(MoFileVO::from);
    }

    /**
     * 根据版本ID查询文件
     * @param versionId 版本ID
     * @return 文件信息
     * @author Xinyp
     */
    public List<MoFileVO> listMoFileByVersionId(Integer versionId){
        return repository.findByVersionId(versionId)
                .stream()
                .map(MoFileVO::from)
                .toList();
    }

    /**
     * 新增文件
     * @param files 文件列表
     * @author Xinyp
     */
    @Deprecated
    @Transactional
    public void add(MultipartFile[] files, @Nullable Integer versionId){
        // 文件是否独立于版本
        List<MoFile> moFiles = saveFiles(files, versionId);

        try {
            repository.saveAll(moFiles);
            if (versionId != null){
                moFileVersionRepository.saveAll(MoFileVersion.batchOf(moFiles, versionId));
            }
        } catch (Exception e){
            moFiles.forEach(fileAssessor::deleteFile);
            MoException.throwOut(ErrorCodes.UNKNOWN, e);
        }
    }

    /**
     * 保存文件到硬盘并返回MoFile对象
     * @param files 文件列表
     * @return MoFile文件列表
     */
    @Deprecated
    @SuppressWarnings("all")
    private List<MoFile> saveFiles(MultipartFile[] files, @Nullable Integer versionId){
        File storageFile;
        List<MoFile> result;
        int len = files.length;
        Path storagePath;

        if (len < 1){
            MoException.throwOut(ErrorCodes.VALIDATION_FAILED, "files can't be empty");
        }
        storagePath = versionId == null ?
                Path.of(STORAGE_PATH, MoConstants.FREE_FILE_DIRECTORY_NAME)
                :
                Path.of(STORAGE_PATH, MoConstants.VERSION_DIRECTORY_NAME, String.valueOf(versionId));
        try {
            Files.createDirectories(storagePath);
        } catch (IOException e) {
            MoException.throwOut(ErrorCodes.UNKNOWN, e);
        }
        result = Lists.newArrayListWithCapacity(len);
        for (MultipartFile file : files) {
            storageFile = new File(storagePath.toString(), RandomUtil.nextStorageName(file.getOriginalFilename()));
            try {
                file.transferTo(storageFile);
            } catch (IOException e) {
                MoException.throwOut(ErrorCodes.UNKNOWN, e);
            }
            result.add(MoFile.of(file, storageFile));
        }

        return result;
    }

    @Transactional
    public void deleteMoFileById(Integer id){
        if (singleReleaseRepository.existsByFileId(id)){
            MoException.throwOut(
                    ErrorCodes.MO_FILE_HAS_SINGLE_RELEASE,
                    "MoFile delete failed, file has singleRelease"
            );
        }
        repository.findById(id)
                .ifPresent(mf -> {
                    fileAssessor.deleteFile(mf);
                    repository.delete(mf);
                    moFileVersionRepository.deleteByIdMoFileId(id);
                });
    }

    @Transactional
    public void deleteMoFileByIds(List<Integer> ids){
        List<MoFile> files;

        if (singleReleaseRepository.existsByFileIdIn(ids)){
            MoException.throwOut(
                    ErrorCodes.MO_FILE_HAS_SINGLE_RELEASE,
                    "MoFile list delete failed, at least one file has singleRelease"
            );
        }
        files = repository.findAllById(ids);
        if (files.isEmpty()){
            return;
        }
        fileAssessor.deleteAllFile(files);
        repository.deleteAllById(ids);
        moFileVersionRepository.deleteByIdMoFileIdIn(ids);
    }

    @Transactional
    public void addMoFilesToVersion(List<Integer> moFileIds, Integer versionId){
        List<MoFile> moFiles;
        Set<Integer> moFileIdsDoNotNeedMove;
        int moFileIdsSize;
        List<MoFileVersion> mfvSaveList;
        List<MoFile> mfSaveList;

        if (!versionRepository.existsById(versionId)){
            MoException.throwOut(ErrorCodes.VALIDATION_FAILED, "versionId=" + versionId + " not exists");
        }
        moFiles = repository.findAllById(moFileIds);
        if (moFiles.isEmpty()){
            return;
        }
        moFileIdsSize = moFileIds.size();
        mfSaveList = Lists.newArrayListWithCapacity(moFileIdsSize);
        // 查询，用于过滤掉无需移动的文件
        moFileIdsDoNotNeedMove = moFileVersionRepository.findIdMoFileIdByIdVersionId(versionId);
        moFiles.stream()
                .filter(mf -> !moFileIdsDoNotNeedMove.contains(mf.getId()))
                .forEach(mf -> {
                    // 文件落盘
                    String newSaveName = RandomUtil.nextStorageName(mf.getOriginName());
                    Path newPath = Path.of(
                            STORAGE_PATH,
                            MoConstants.VERSION_DIRECTORY_NAME,
                            String.valueOf(versionId),
                            newSaveName
                    );

                    try {
                        Files.copy(
                                Path.of(mf.getPath()),
                                newPath
                        );
                        // 准备插入数据库的文件信息
                        mfSaveList.add(MoFile.of(mf, newPath, newSaveName));
                    } catch (IOException e) {
                        MoException.throwOut(ErrorCodes.UNKNOWN, e);
                    }
                });
        // 操作数据库
        try {
            if (!mfSaveList.isEmpty()){
                repository.saveAll(mfSaveList);
                mfvSaveList = mfSaveList.stream().map(mf -> MoFileVersion.of(mf.getId(), versionId)).toList();
                moFileVersionRepository.saveAll(mfvSaveList);
            }
        }catch (Exception e){
            fileAssessor.deleteAllFile(mfSaveList);
            MoException.throwOut(ErrorCodes.UNKNOWN, e);
        }
    }

    /**
     * 根据单发布 CDK code 下载
     * @param code 单发布CDK的code
     */
    @Transactional
    public void downloadBySingleReleaseCDKCodeAndReleaseId(String code, Integer releaseId){
        AtomicBoolean success = new AtomicBoolean(false);

        singleReleaseCDKRepository.findByCode(code).ifPresent(cdk -> {
            validateAndUpdateSingleReleaseCDK(cdk);
            singleReleaseCDKASORepository.findByIdCdkIdAndIdSrId(cdk.getId(), releaseId)
                    .flatMap(
                            aso -> singleReleaseRepository.findById(aso.getId().getSrId())
                    ).ifPresent(r -> {
                        validateSingleRelease(r);
                        repository.findById(r.getFileId()).ifPresent(moFile -> {
                            updateSingleRelease(r);
                            updateSingleReleaseCDK(cdk);
                            singleReleaseRepository.save(r);
                            singleReleaseCDKRepository.save(cdk);
                            ServletUtil.sendResponse(moFile.toFile(), moFile.getOriginName());
                        });
                        success.set(true);
                    });
        });
        if (!success.get()){
            MoException.throwOut(
                    ErrorCodes.FORBIDDEN,
                    "Unable to download file, cdk code: " + code + ", srId: " + releaseId
            );
        }
    }

    private void validateAndUpdateSingleReleaseCDK(SingleReleaseCDK cdk){
        LocalDateTime now;

        if (cdk.getCdkStatus() == SingleReleaseCDKStatus.BANNED){
            MoException.throwOut(
                    ErrorCodes.FORBIDDEN,
                    "Download file failed, SingleReleaseCDK is banned, " + cdk
            );
        }
        now = LocalDateTime.now();
        if (now.isAfter(cdk.getExpireAt())){
            MoException.throwOut(
                    ErrorCodes.FORBIDDEN,
                    "Download file failed, SingleReleaseCDK is expired, " + cdk
            );
        }
        validateOrUpdateMoGhostAuthWord(cdk);
    }

    private void validateOrUpdateMoGhostAuthWord(SingleReleaseCDK cdk){
        String authWord = ServletUtil.getRequestHeader(SecurityConstants.REQ_HEADER_MO_GHOST_AUTH_ID);
        String correctAuthWord;

        if (StringUtils.isBlank(authWord) || SecurityConstants.ANDROID_INVALID_MAC.equals(authWord)){
            // 请求未携带身份标识或携带了无效身份标识
            MoException.throwOut(
                    ErrorCodes.FORBIDDEN,
                    "Download file failed, auth word '" + authWord + "' invalid"
            );
        }else{
            correctAuthWord = cdk.getAuthWord();
            if (StringUtils.isBlank(correctAuthWord)){
                // 当前CDK未设置身份标识（新CDK）
                if (singleReleaseCDKRepository.existsByAuthWord(authWord)){
                    MoException.throwOut(
                            ErrorCodes.FORBIDDEN,
                            "Download file failed, new auth word duplicated"
                    );
                }
                cdk.setAuthWord(authWord);
            } else if (!correctAuthWord.equals(authWord)){
                // 请求中的身份标识与CDK的不符
                MoException.throwOut(
                        ErrorCodes.FORBIDDEN,
                        "Download file failed, wrong auth word"
                );
            }
        }
    }

    private void validateSingleRelease(SingleRelease r){
        if (r.getReleaseStatus() != SingleReleaseStatus.PUBLISHED){
            MoException.throwOut(
                    ErrorCodes.FORBIDDEN,
                    "Download file failed, SingleRelease is not published, " + r
            );
        }
    }

    private void updateSingleReleaseCDK(SingleReleaseCDK cdk){
        Pair<String, String> ipAndRegion = ipLocator.locate();

        cdk.setLastAccessAt(LocalDateTime.now());
        cdk.setTotalAccess(cdk.getTotalAccess() + 1);
        cdk.setLastAccessIP(ipAndRegion.getFirst());
        cdk.setLastAccessRegion(ipAndRegion.getSecond());
    }

    private void updateSingleRelease(SingleRelease singleRelease){
        singleRelease.setTotalAccess(singleRelease.getTotalAccess() + 1);
    }

    /**
     * 根据文件ID下载
     * @param id ID
     */
    public void downloadById(Integer id){
        repository.findById(id)
                .ifPresent(file -> ServletUtil.sendResponse(file.toFile(), file.getOriginName()));
    }

    /**
     * 根据文件ID更名
     * @param id ID
     * @param name 新名称
     */
    public void renameById(Integer id, String name){
        int len = name.length();

        if (len > 64){
            MoException.throwOut(
                    ErrorCodes.VALIDATION_FAILED,
                    "file new originName is too long: " + len
            );
        }
        repository.findById(id)
                .ifPresent(file -> {
                    if (name.equals(file.getOriginName())){
                        return;
                    }
                    file.setOriginName(name);
                    repository.save(file);
                });
    }

    /**
     * 根据ID更新备注
     * @param id ID
     * @param newRemark 新备注
     */
    public void updateRemarkById(Integer id, String newRemark){
        int len = newRemark.length();

        if (len > 64){
            MoException.throwOut(
                    ErrorCodes.VALIDATION_FAILED,
                    "file new remark is too long: " + len
            );
        }
        repository.findById(id)
                .ifPresent(file -> {
                    if (newRemark.equals(file.getRemark())){
                        return;
                    }
                    file.setRemark(newRemark);
                    repository.save(file);
                });
    }

    /**
     * 替换文件
     * @param file 上传的文件
     * @param id 要替换的文件信息ID
     */
    public void replace(MultipartFile file, Integer id){
        String fileName = file.getOriginalFilename();

        if (StringUtils.isBlank(fileName)){
            return;
        }
        repository.findById(id)
                .ifPresent(moFile -> {
                    MoFileVersion mv = moFileVersionRepository.findByIdMoFileId(id).orElse(null);
                    String newSaveName = RandomUtil.nextStorageName(fileName);
                    Path newPath = mv == null ?
                            Path.of(
                                    STORAGE_PATH,
                                    MoConstants.FREE_FILE_DIRECTORY_NAME,
                                    newSaveName
                            )
                            :
                            Path.of(
                                    STORAGE_PATH,
                                    MoConstants.VERSION_DIRECTORY_NAME,
                                    String.valueOf(mv.getId().getVersionId()),
                                    newSaveName
                            );

                    // 删除旧文件
                    fileAssessor.deleteFile(moFile);
                    // 存储新文件、更新对象数据
                    moFile.setPath(newPath.toAbsolutePath().toString());
                    moFile.setOriginName(fileName);
                    try {
                        Files.copy(
                                file.getInputStream(),
                                newPath
                        );
                    } catch (IOException e) {
                        MoException.throwOut(ErrorCodes.UNKNOWN, e);
                    }
                    moFile.refresh();
                    // 准备插入数据库的文件信息
                    repository.save(moFile);
                });
    }

    /**
     * 根据访问Key下载
     * @param accessKey 访问key
     * @param id 文件信息ID
     */
    public void downloadByAccessKey(String accessKey, Integer id) {
        if (!accessKeyVerifier.verify(accessKey)) {
            MoException.throwOut(
                    ErrorCodes.FORBIDDEN,
                    "Unable to download file, accessKey: " + accessKey + ", fileId: " + id
            );
        }
        repository.findById(id)
                .ifPresent(file -> ServletUtil.sendResponse(file.toFile(), file.getOriginName()));
    }
}

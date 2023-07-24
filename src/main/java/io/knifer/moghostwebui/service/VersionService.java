package io.knifer.moghostwebui.service;

import io.knifer.moghostwebui.common.constant.ErrorCodes;
import io.knifer.moghostwebui.common.constant.MoConstants;
import io.knifer.moghostwebui.common.constant.VersionStatus;
import io.knifer.moghostwebui.common.constant.VersionUpdatingType;
import io.knifer.moghostwebui.common.entity.domain.MoFile;
import io.knifer.moghostwebui.common.entity.domain.Package;
import io.knifer.moghostwebui.common.entity.domain.Version;
import io.knifer.moghostwebui.common.entity.request.VersionAddRequest;
import io.knifer.moghostwebui.common.entity.request.VersionInfoUpdateRequest;
import io.knifer.moghostwebui.common.entity.request.VersionQueryRequest;
import io.knifer.moghostwebui.common.entity.vo.VersionVO;
import io.knifer.moghostwebui.common.exception.MoException;
import io.knifer.moghostwebui.common.tool.file.FileAssessor;
import io.knifer.moghostwebui.common.transaction.TransactionExecutor;
import io.knifer.moghostwebui.config.properties.MoGhostProperties;
import io.knifer.moghostwebui.repository.*;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * 版本服务
 *
 * @author Knifer
 * @version 1.0.0
 */
@Service
public class VersionService {

    private final VersionRepository repository;
    private final ProjectRepository projectRepository;
    private final MoFileVersionRepository moFileVersionRepository;
    private final MoFileRepository moFileRepository;
    private final FileAssessor fileAssessor;
    private final TransactionExecutor transactionExecutor;
    private final String STORAGE_PATH;
    private final PackageRepository packageRepository;

    @Autowired
    public VersionService(
            VersionRepository repository,
            ProjectRepository projectRepository,
            MoFileVersionRepository moFileVersionRepository,
            MoFileRepository moFileRepository,
            FileAssessor fileAssessor,
            TransactionExecutor transactionExecutor,
            MoGhostProperties.StorageProperties props,
            PackageRepository packageRepository) {
        this.repository = repository;
        this.projectRepository = projectRepository;
        this.moFileVersionRepository = moFileVersionRepository;
        this.moFileRepository = moFileRepository;
        this.fileAssessor = fileAssessor;
        this.transactionExecutor = transactionExecutor;
        this.STORAGE_PATH = props.getSavePath();
        this.packageRepository = packageRepository;
    }

    /**
     * 获取版本列表
     * @param pageRequest 分页对象
     * @param queryRequest 查询对象
     * @return 分页数据
     */
    public Page<VersionVO> getVersionList(PageRequest pageRequest, VersionQueryRequest queryRequest){
        return repository.findAll(
                queryRequest.toExample(),
                pageRequest
        ).map(VersionVO::from);
    }

    /**
     * 更新版本状态
     * @param id 版本ID
     * @param newStatus 版本状态
     */
    public void updateVersionStatus(Integer id, VersionStatus newStatus){
        repository.findById(id)
                .ifPresent(v -> {
                    VersionStatus status = v.getStatus();

                    if (status == newStatus || status == VersionStatus.PUBLISHED){
                        return;
                    }
                    repository.updateStatusById(newStatus, id);
                });
    }

    /**
     * 预发布版本（创建包信息）
     * @param id 版本ID
     * @param name 要创建的包名称
     * @return 版本对象+包对象
     */
    public Pair<Version, Package> prePublishVersion(Integer id, String name){
        Version v = repository.findById(id)
                .orElseThrow(() -> MoException.just(ErrorCodes.VALIDATION_FAILED, "version not exist"));
        Package p = Package.of(v, StringUtils.isBlank(name) ? MoConstants.DEFAULT : name, STORAGE_PATH);

        packageRepository.save(p);

        return Pair.of(v, p);
    }

    /**
     * 发布版本
     * @param id 版本ID
     * @param versionAndPackage 要发布的版本和对应的资源包对象
     */
    @Async
    @SuppressWarnings("all")
    public void publishVersion(Pair<Version, Package> versionAndPackage){
        Version v = versionAndPackage.getFirst();
        Package p = versionAndPackage.getSecond();

        transactionExecutor.begin();
        try {
            // 进行打包并输出到目录
            makePackage(v);
            // 刷新Pakcage对象状态
            p.refresh();
            // 落库
            packageRepository.save(p);
            // 更新版本状态信息
            if (v.getStatus() != VersionStatus.PUBLISHED){
                v.setStatus(VersionStatus.PUBLISHED);
                repository.save(v);
            }
        } catch (Exception e){
            transactionExecutor.rollback();
            new File(p.getSavePath()).delete();
            MoException.throwOut(ErrorCodes.UNKNOWN, e);
        }
        transactionExecutor.commit();
    }

    /**
     * 更新版本更新类型
     * @param id 版本ID
     * @param newUpdatingType 版本更新类型
     */
    public void updateVersionUpdatingType(Integer id, VersionUpdatingType newUpdatingType){
        repository.findById(id)
                .ifPresent(v -> {
                    VersionUpdatingType updatingType = v.getUpdatingType();

                    if (updatingType == newUpdatingType){
                        return;
                    }
                    repository.updateUpdatingTypeById(newUpdatingType, id);
                });
    }

    /**
     * 更新版本信息
     * @param id 版本ID
     * @param request 请求参数
     */
    public void updateVersionInfo(Integer id, VersionInfoUpdateRequest request){
        repository.findById(id)
                .ifPresent(v -> {
                    String newName = request.getName();

                    if (newName.equals(v.getName())){
                        return;
                    }
                    if (repository.existsByNameAndProjectIdAndIdNot(newName, v.getProjectId(), id)){
                        MoException.throwOut(ErrorCodes.VALIDATION_DUPLICATED, "version duplicated");
                    }
                    repository.updateNameById(newName, id);
                });
    }

    /**
     * 新增版本
     * @param request 请求参数
     */
    public void add(VersionAddRequest request){
        Version v;
        Integer projectId = request.getProjectId();

        if (!projectRepository.existsById(projectId)){
            MoException.throwOut(ErrorCodes.VALIDATION_FAILED, "project not found");
        }
        if (repository.existsByNameAndProjectId(request.getName(), projectId)){
            MoException.throwOut(ErrorCodes.VALIDATION_DUPLICATED, "version duplicated");
        }
        v = Version.from(request);
        try {
            Files.createDirectories(Path.of(STORAGE_PATH, MoConstants.VERSION_DIRECTORY_NAME, String.valueOf(v.getId())));
        } catch (IOException e) {
            MoException.throwOut(ErrorCodes.UNKNOWN, e);
        }

        repository.save(v);
    }

    /**
     * 删除版本
     * @param versionId 版本ID
     */
    @Transactional
    public void delete(Integer versionId){
        List<MoFile> files;

        if (!repository.existsById(versionId)){
            return;
        }
        files = moFileRepository.findByVersionId(versionId);
        moFileRepository.deleteAllInBatch(files);
        moFileVersionRepository.deleteByIdVersionId(versionId);
        repository.deleteById(versionId);
        files.forEach(fileAssessor::deleteFile);

    }

    /**
     * 打包并更新Package对象（hashValue、ready）
     * @param version 版本
     */
    private void makePackage(Version version){
        List<MoFile> files;
        Integer versionId = version.getId();
        String versionIdStr;
        String outputPath;

        files = moFileRepository.findByVersionId(versionId);
        if (files.isEmpty()){
            MoException.throwOut(ErrorCodes.VALIDATION_FAILED, "no file to package, versionId=" + versionId);
        }
        files.forEach(f -> {
            File file = f.toFile();

            if (!file.exists()){
                MoException.throwOut(ErrorCodes.UNKNOWN, "file \"" + file.getAbsolutePath() + "\" not exists");
            }
        });
        versionIdStr = String.valueOf(versionId);
        outputPath = Path.of(STORAGE_PATH, MoConstants.PACKAGE_DIRECTORY_NAME, versionIdStr).toString();
        // 输出压缩文件到目录
        fileAssessor.compress(
                version.createSavePath(STORAGE_PATH),
                outputPath
        );
    }
}

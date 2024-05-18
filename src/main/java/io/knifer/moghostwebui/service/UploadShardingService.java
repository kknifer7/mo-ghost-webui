package io.knifer.moghostwebui.service;

import com.google.common.collect.ImmutableList;
import io.knifer.moghostwebui.common.constant.ErrorCodes;
import io.knifer.moghostwebui.common.constant.MoConstants;
import io.knifer.moghostwebui.common.entity.domain.UploadSharding;
import io.knifer.moghostwebui.common.entity.request.UploadShardingRequest;
import io.knifer.moghostwebui.common.entity.vo.UploadShardingVO;
import io.knifer.moghostwebui.common.entity.vo.ValueVO;
import io.knifer.moghostwebui.common.exception.MoException;
import io.knifer.moghostwebui.common.tool.file.FileAssessor;
import io.knifer.moghostwebui.common.util.CodecUtil;
import io.knifer.moghostwebui.common.util.FileUtil;
import io.knifer.moghostwebui.config.properties.MoGhostProperties;
import io.knifer.moghostwebui.handle.upload.UploadShardingProcessor;
import io.knifer.moghostwebui.repository.UploadShardingRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * 分片上传服务
 *
 * @author Knifer
 * @version 1.0.0
 */
@Service
public class UploadShardingService {

    private final UploadShardingRepository repository;

    private final FileAssessor fileAssessor;

    private final String WORKING_PATH;

    private final Collection<UploadShardingProcessor<?>> processors;

    @Autowired
    public UploadShardingService(
            UploadShardingRepository repository,
            FileAssessor fileAssessor,
            MoGhostProperties.StorageProperties storageProperties,
            Collection<UploadShardingProcessor<?>> processors
    ){
        String savePathProp = storageProperties.getSavePath();

        this.repository = repository;
        this.fileAssessor = fileAssessor;
        this.WORKING_PATH = StringUtils.appendIfMissing(
                savePathProp + MoConstants.WORKING_STORAGE_DIRECTORY_NAME, "/"
        );
        this.processors = processors;
    }

    /**
     * 查询上传分片信息列表
     * @return 分片信息列表
     */
    public List<UploadShardingVO> findAll() {
        return repository.findAll().stream().map(UploadShardingVO::from).toList();
    }

    /**
     * 删除分片信息（包括所有文件）
     * @param id 分片信息ID
     */
    public void removeById(Integer id) {
        repository.findById(id)
                .ifPresent(sharding -> {
                    fileAssessor.deleteFile(sharding);
                    repository.delete(sharding);
                });
    }

    /**
     * 根据文件Key删除
     * @param fileKey 文件Key
     */
    @Transactional
    public void removeByFileKey(String fileKey) {
        repository.deleteByFileKey(fileKey);
    }

    /**
     * 移除所有已完成的分片信息
     */
    @Transactional
    public void removeAllDone() {
        repository.deleteByDoneFlagTrue();
    }

    /**
     * 上传文件
     * @param request 上传请求
     * @return 分片信息
     */
    @Transactional
    @SuppressWarnings("all")
    public UploadShardingVO upload(UploadShardingRequest request) {
        String fileKey = request.getFileKey();
        UploadSharding uploadSharding = repository.findByFileKey(fileKey).orElse(null);
        String filePath = WORKING_PATH + fileKey;
        String shardPath = filePath + "." + request.getShardIndex();
        Integer shardIdx, shardCount;

        if (uploadSharding == null) {
            uploadSharding = UploadSharding.of(request, filePath);
        }
        try {
            fileAssessor.saveBytes(
                    getBytesAndValidateUploadShardingRequest(request, uploadSharding),
                    shardPath
            );
            uploadSharding.setShardIndex(uploadSharding.getShardIndex() + 1);
            uploadSharding = repository.save(uploadSharding);
            shardIdx = uploadSharding.getShardIndex();
            shardCount = uploadSharding.getShardCount();
            if (shardIdx.equals(shardCount)) {
                for (UploadShardingProcessor p : processors) {
                    if (p.support(uploadSharding)) {
                        p.prepare(request.getFileId());
                        p.process(uploadSharding);
                    }
                }
            }
        } catch (Exception e) {
            new File(shardPath).delete();
            new File(filePath).delete();
            if (e instanceof MoException me && me.getCode() == ErrorCodes.VALIDATION_FAILED) {
                throw me;
            }
            MoException.throwOut(ErrorCodes.UNKNOWN, e);
        }

        return UploadShardingVO.from(uploadSharding);
    }

    private byte[] getBytesAndValidateUploadShardingRequest(UploadShardingRequest request, UploadSharding po) {
        Integer shardIndex = request.getShardIndex();
        String fileName;
        int shardSize;
        byte[] result;

        if (!shardIndex.equals(po.getShardIndex())){
            MoException.throwOut(ErrorCodes.VALIDATION_FAILED);
        }
        fileName = request.getFileName();
        if (!FileUtil.isFileNameSafe(fileName)) {
            MoException.throwOut(ErrorCodes.SECURITY_CHECK_FAILED, "File name check failed.");
        }
        result = CodecUtil.base64(request.getShardContent());
        shardSize = request.getShardSize();
        if (
                (result.length < shardSize && shardIndex + 1 < po.getShardCount()) ||
                result.length > shardSize
        ) {
            MoException.throwOut(ErrorCodes.VALIDATION_FAILED);
        }

        return result;
    }

    /**
     * 获取指定FileKey缺失的分片
     * @param fileKey 文件唯一标识
     * @return 缺失的分片号列表
     */
    public ValueVO<List<Integer>> findMissingParts(String fileKey){

        return repository.findByFileKey(fileKey)
                .map(sharding -> ValueVO.from(fileAssessor.findMissingShards(sharding)))
                .orElseGet(() -> ValueVO.from(ImmutableList.of()));
    }

    /**
     * 根据ID获取文件KEY
     * @param id 分片ID
     * @return 文件KEY
     */
    public ValueVO<String> findFileKeyById(Integer id) {
        return repository.findById(id)
                .map(s -> ValueVO.from(s.getFileKey()))
                .orElseGet(() -> ValueVO.from(Strings.EMPTY));
    }
}

package io.knifer.moghostwebui.handle.upload.impl;

import io.knifer.moghostwebui.common.constant.BusinessType;
import io.knifer.moghostwebui.common.constant.ErrorCodes;
import io.knifer.moghostwebui.common.constant.MoConstants;
import io.knifer.moghostwebui.common.entity.domain.MoFile;
import io.knifer.moghostwebui.common.entity.domain.MoFileVersion;
import io.knifer.moghostwebui.common.entity.domain.UploadSharding;
import io.knifer.moghostwebui.common.exception.MoException;
import io.knifer.moghostwebui.common.tool.file.FileAssessor;
import io.knifer.moghostwebui.common.transaction.TransactionExecutor;
import io.knifer.moghostwebui.common.util.RandomUtil;
import io.knifer.moghostwebui.config.properties.MoGhostProperties;
import io.knifer.moghostwebui.handle.upload.UploadShardingProcessor;
import io.knifer.moghostwebui.repository.MoFileRepository;
import io.knifer.moghostwebui.repository.MoFileVersionRepository;
import io.knifer.moghostwebui.repository.UploadShardingRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 单文件替换分片合并处理器
 *
 * @author Knifer
 * @version 1.0.0
 */
@Slf4j
@Component
public class MoFileReplaceUploadShardingProcessor implements UploadShardingProcessor<Integer> {
    private final UploadShardingRepository uploadShardingRepository;

    private final ThreadLocal<Integer> moFileIdThreadLocal;

    private final String STORAGE_PATH;

    private final MoFileRepository moFileRepository;

    private final MoFileVersionRepository moFileVersionRepository;

    private final FileAssessor fileAssessor;

    private final TransactionExecutor transactionExecutor;

    private final AsyncTaskExecutor asyncTaskExecutor;

    public MoFileReplaceUploadShardingProcessor(
            MoGhostProperties.StorageProperties props,
            MoFileRepository moFileRepository,
            MoFileVersionRepository moFileVersionRepository,
            UploadShardingRepository uploadShardingRepository,
            FileAssessor fileAssessor,
            TransactionExecutor transactionExecutor,
            AsyncTaskExecutor asyncTaskExecutor
    ) {
        this.moFileIdThreadLocal = new ThreadLocal<>();
        this.STORAGE_PATH = StringUtils.appendIfMissing(props.getSavePath(), "/");
        this.moFileRepository = moFileRepository;
        this.moFileVersionRepository = moFileVersionRepository;
        this.uploadShardingRepository = uploadShardingRepository;
        this.fileAssessor = fileAssessor;
        this.transactionExecutor = transactionExecutor;
        this.asyncTaskExecutor = asyncTaskExecutor;
    }

    @Override
    public boolean support(UploadSharding sharding) {
        return sharding.getBusinessType() == BusinessType.SINGLE_RELEASE_FILE_REPLACE;
    }

    @Override
    public void prepare(Integer moFileId) {
        moFileIdThreadLocal.set(moFileId);
    }

    @Override
    public void process(UploadSharding uploadSharding) {
        Integer moFileId = moFileIdThreadLocal.get();

        moFileIdThreadLocal.remove();
        asyncTaskExecutor.submit(() -> {
            MoFile oldMoFile;
            Path savePath;
            String savePathStr;
            String newSaveName;
            String newOriginName;
            boolean mergeSuccessFlag;
            MoFileVersion version;

            if (moFileId == null) {
                MoException.throwOut(ErrorCodes.UNKNOWN, "moFileId is null");
                return;
            }
            oldMoFile = moFileRepository.findById(moFileId).orElse(null);
            if (oldMoFile == null) {
                MoException.throwOut(ErrorCodes.UNKNOWN, "file not found, id={}");
                return;
            }
            version = moFileVersionRepository.findByIdMoFileId(moFileId).orElse(null);
            newOriginName = uploadSharding.getFileName();
            newSaveName = RandomUtil.nextStorageName(newOriginName);
            savePath = version == null ?
                    Path.of(
                            STORAGE_PATH,
                            MoConstants.FREE_FILE_DIRECTORY_NAME,
                            newSaveName
                    )
                    :
                    Path.of(
                            STORAGE_PATH,
                            MoConstants.VERSION_DIRECTORY_NAME,
                            String.valueOf(version.getId().getVersionId()),
                            newSaveName
                    );
            savePathStr = savePath.toString();
            mergeSuccessFlag = fileAssessor.mergeShard(uploadSharding, savePathStr);
            if (!mergeSuccessFlag) {
                MoException.throwOut(ErrorCodes.UNKNOWN, "merge shard failed");
            }
            try {
                // 删除旧文件
                fileAssessor.deleteFile(oldMoFile);
                // 存储新文件、更新对象数据
                oldMoFile.setPath(savePathStr);
                oldMoFile.setOriginName(newOriginName);
                oldMoFile.refresh();
                uploadSharding.setDoneFlag(true);
                // 更新
                transactionExecutor.begin();
                try {
                    moFileRepository.save(oldMoFile);
                    uploadShardingRepository.save(uploadSharding);
                } catch (Exception e) {
                    transactionExecutor.rollback();
                    MoException.throwOut(ErrorCodes.UNKNOWN, e);
                }
                transactionExecutor.commit();
            } catch (Exception e) {
                uploadSharding.setDoneFlag(false);
                uploadShardingRepository.save(uploadSharding);
                try {
                    Files.delete(savePath);
                } catch (IOException ex) {
                    MoException.throwOut(ErrorCodes.UNKNOWN, ex);
                }
                MoException.throwOut(ErrorCodes.UNKNOWN, e);
            }
        });
    }
}

package io.knifer.moghostwebui.handle.upload.impl;

import io.knifer.moghostwebui.common.constant.BusinessType;
import io.knifer.moghostwebui.common.constant.ErrorCodes;
import io.knifer.moghostwebui.common.constant.MoConstants;
import io.knifer.moghostwebui.common.constant.MoFileState;
import io.knifer.moghostwebui.common.entity.domain.MoFile;
import io.knifer.moghostwebui.common.entity.domain.UploadSharding;
import io.knifer.moghostwebui.common.exception.MoException;
import io.knifer.moghostwebui.common.tool.file.FileAssessor;
import io.knifer.moghostwebui.common.transaction.TransactionExecutor;
import io.knifer.moghostwebui.common.util.RandomUtil;
import io.knifer.moghostwebui.config.properties.MoGhostProperties;
import io.knifer.moghostwebui.handle.upload.UploadShardingProcessor;
import io.knifer.moghostwebui.repository.MoFileRepository;
import io.knifer.moghostwebui.repository.UploadShardingRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;

/**
 * 单文件分片合并处理器
 *
 * @author Knifer
 * @version 1.0.0
 */
@Slf4j
@Component
public class MoFileUploadShardingProcessor implements UploadShardingProcessor<Void> {

    private final String FREE_FILE_SAVE_PATH;

    private final MoFileRepository moFileRepository;

    private final UploadShardingRepository uploadShardingRepository;

    private final AsyncTaskExecutor asyncTaskExecutor;

    private final FileAssessor fileAssessor;

    private final TransactionExecutor transactionExecutor;

    public MoFileUploadShardingProcessor(
            MoGhostProperties.StorageProperties props,
            MoFileRepository moFileRepository,
            UploadShardingRepository uploadShardingRepository,
            AsyncTaskExecutor asyncTaskExecutor,
            FileAssessor fileAssessor,
            TransactionExecutor transactionExecutor
    ) {
        this.FREE_FILE_SAVE_PATH = StringUtils.appendIfMissing(props.getSavePath(), "/") +
                MoConstants.FREE_FILE_DIRECTORY_NAME;
        this.moFileRepository = moFileRepository;
        this.uploadShardingRepository = uploadShardingRepository;
        this.asyncTaskExecutor = asyncTaskExecutor;
        this.fileAssessor = fileAssessor;
        this.transactionExecutor = transactionExecutor;
    }

    @Override
    public boolean support(UploadSharding sharding) {
        return sharding.getBusinessType() == BusinessType.SINGLE_RELEASE;
    }

    @Override
    public void prepare(Void data) {}

    @Override
    public void process(UploadSharding uploadSharding) {
        String savePath = Paths.get(
                FREE_FILE_SAVE_PATH,
                RandomUtil.nextStorageName(uploadSharding.getFileName())
        ).toAbsolutePath().toString();
        MoFile newFile = moFileRepository.save(MoFile.of(uploadSharding, savePath));

        asyncTaskExecutor.submit(() -> mergeFile(uploadSharding, newFile));
    }

    private void mergeFile(UploadSharding sharding, MoFile moFile) {
        String savePath = moFile.getPath();
        boolean success;

        try {
            success = fileAssessor.mergeShard(sharding, savePath);
            if (!success) {
                MoException.throwOut(ErrorCodes.VALIDATION_FAILED, "file merge failed");
            }
        } catch (Exception e) {
            log.error("file merge failed", e);
            moFile.setState(MoFileState.MERGE_FAILED);
            moFileRepository.save(moFile);

            return;
        }
        moFile.refresh();
        moFile.setState(MoFileState.NORMAL);
        transactionExecutor.execute(() -> {
            moFileRepository.save(moFile);
            sharding.setDoneFlag(true);
            uploadShardingRepository.save(sharding);
        });
        log.info("new file: {}", moFile);
    }
}

package io.knifer.moghostwebui.service;

import io.knifer.moghostwebui.common.constant.ErrorCodes;
import io.knifer.moghostwebui.common.entity.domain.MoFileVersion;
import io.knifer.moghostwebui.common.exception.MoException;
import io.knifer.moghostwebui.repository.MoFileVersionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 文件-版本服务
 *
 * @author Knifer
 * @version 1.0.0
 */
@Service
@AllArgsConstructor
public class MoFileVersionService {

    private final MoFileVersionRepository repository;

    /**
     * 绑定现有文件与版本
     * @param moFileId 文件ID
     * @param versionId 版本ID
     */
    public void bindMoFileAndVersion(Integer moFileId, Integer versionId){
        if (repository.existsByIdMoFileIdAndIdVersionId(moFileId, versionId)){
            MoException.throwOut(ErrorCodes.VALIDATION_DUPLICATED, "MoFileVersion duplicated");
        }

        repository.save(MoFileVersion.of(moFileId, versionId));
    }
}

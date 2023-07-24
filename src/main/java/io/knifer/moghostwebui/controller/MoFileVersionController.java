package io.knifer.moghostwebui.controller;

import io.knifer.moghostwebui.common.entity.vo.ApiResult;
import io.knifer.moghostwebui.service.MoFileVersionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文件-版本关联
 *
 * @author Knifer
 * @version 1.0.0
 */
@RestController
@RequestMapping("/mo-file-version")
@AllArgsConstructor
public class MoFileVersionController {

    private final MoFileVersionService service;

    @PostMapping("/{moFileId}/{versionId}")
    public ApiResult<Void> bindMoFileAndVersion(
            @PathVariable("moFileId") Integer moFileId,
            @PathVariable("versionId") Integer versionId
    ){
        service.bindMoFileAndVersion(moFileId, versionId);

        return ApiResult.ok();
    }
}

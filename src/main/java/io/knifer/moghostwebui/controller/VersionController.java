package io.knifer.moghostwebui.controller;

import io.knifer.moghostwebui.common.constant.VersionStatus;
import io.knifer.moghostwebui.common.constant.VersionUpdatingType;
import io.knifer.moghostwebui.common.entity.domain.Package;
import io.knifer.moghostwebui.common.entity.domain.Version;
import io.knifer.moghostwebui.common.entity.request.PageParams;
import io.knifer.moghostwebui.common.entity.request.VersionAddRequest;
import io.knifer.moghostwebui.common.entity.request.VersionQueryRequest;
import io.knifer.moghostwebui.common.entity.vo.ApiResult;
import io.knifer.moghostwebui.common.entity.vo.VersionVO;
import io.knifer.moghostwebui.service.VersionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;

/**
 * 版本
 *
 * @author Knifer
 * @version 1.0.0
 */
@RestController
@AllArgsConstructor
@RequestMapping("/version")
public class VersionController {

    private final VersionService service;

    /**
     * 获取版本列表
     * @param page 分页参数
     * @param request 查询参数
     * @return 分页数据
     */
    @PostMapping("/page")
    public ApiResult<Page<VersionVO>> getVersionList(
            PageParams page,
            @Valid @RequestBody VersionQueryRequest request
    ){
        return ApiResult.ok(service.getVersionList(page.toPageRequest(), request));
    }

    /**
     * 新增版本
     * @param request 请求参数
     * @return void
     */
    @PostMapping("/")
    public ApiResult<Void> addVersion(@Valid @RequestBody VersionAddRequest request){
        service.add(request);

        return ApiResult.ok();
    }

    /**
     * 删除版本
     * @param versionId 版本ID
     * @return void
     */
    @DeleteMapping("/{versionId}")
    public ApiResult<Void> deleteByVersionId(@PathVariable("versionId") Integer versionId){
        service.delete(versionId);

        return ApiResult.ok();
    }

    /**
     * 更新版本状态
     * @param versionId 版本ID
     * @param status 版本状态
     * @return void
     */
    @PatchMapping("/status/{versionId}/{status}")
    public ApiResult<Void> updateVersionStatusById(
            @PathVariable("versionId") Integer versionId,
            @PathVariable("status") VersionStatus status
    ){
        service.updateVersionStatus(versionId, status);

        return ApiResult.ok();
    }

    /**
     * 发布版本
     * @param versionId 版本ID
     * @param name 要创建的包名称
     * @return void
     */
    @PatchMapping("/publish/{versionId}/{name}")
    public ApiResult<Void> publishVersionById(
            @PathVariable("versionId") Integer versionId,
            @PathVariable("name") String name
    ){
        // 预发布处理
        Pair<Version, Package> pair = service.prePublishVersion(versionId, name);

        // 异步处理发布逻辑
        service.publishVersion(pair);

        return ApiResult.ok();
    }

    /**
     * 更新版本类型
     * @param versionId 版本ID
     * @param type 版本类型
     * @return void
     */
    @PatchMapping("/type/{versionId}/{type}")
    public ApiResult<Void> updateVersionTypeById(
            @PathVariable("versionId") Integer versionId,
            @PathVariable("type") VersionUpdatingType type
    ){
        service.updateVersionUpdatingType(versionId, type);

        return ApiResult.ok();
    }
}

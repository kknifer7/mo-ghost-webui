package io.knifer.moghostwebui.controller;

import io.knifer.moghostwebui.common.entity.request.PageParams;
import io.knifer.moghostwebui.common.entity.request.SingleReleaseCDKAddUpdateRequest;
import io.knifer.moghostwebui.common.entity.request.SingleReleaseCDKQueryRequest;
import io.knifer.moghostwebui.common.entity.vo.ApiResult;
import io.knifer.moghostwebui.common.entity.vo.SingleReleaseCDKVO;
import io.knifer.moghostwebui.service.SingleReleaseCDKService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 单发布CDK
 *
 * @author Knifer
 * @version 1.0.0
 */
@RestController
@RequestMapping("/single-release-cdk")
@AllArgsConstructor
public class SingleReleaseCDKController {

    private final SingleReleaseCDKService service;

    /**
     * 新增
     * @param request 请求参数
     * @return void
     */
    @PostMapping("/")
    public ApiResult<Void> add(@Valid @RequestBody SingleReleaseCDKAddUpdateRequest request){
        service.add(request);

        return ApiResult.ok();
    }

    /**
     * 更新
     * @param request 请求参数
     * @return void
     */
    @PatchMapping("/")
    public ApiResult<Void> update(@Valid @RequestBody SingleReleaseCDKAddUpdateRequest request){
        service.update(request);

        return ApiResult.ok();
    }

    /**
     * 删除
     * @param id ID
     * @return void
     */
    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable("id") Integer id){
        service.deleteById(id);

        return ApiResult.ok();
    }

    /**
     * 分页查询
     * @param pageParams 分页对象
     * @param request 请求参数
     * @return void
     */
    @GetMapping("/")
    public ApiResult<Page<SingleReleaseCDKVO>> findPage(PageParams pageParams, SingleReleaseCDKQueryRequest request){
        return ApiResult.ok(service.listPage(pageParams, request));
    }

    /**
     * 删除CDK权限标识（解绑设备）
     * @param cdkId ID
     * @return void
     */
    @DeleteMapping("/auth-word/{id}")
    public ApiResult<Void> deleteAuthWord(@PathVariable("id") Integer cdkId){
        service.deleteAuthWordById(cdkId);

        return ApiResult.ok();
    }
}

package io.knifer.moghostwebui.controller;

import io.knifer.moghostwebui.common.entity.request.PageParams;
import io.knifer.moghostwebui.common.entity.request.SingleReleaseAddUpdateRequest;
import io.knifer.moghostwebui.common.entity.vo.ApiResult;
import io.knifer.moghostwebui.common.entity.vo.SingleReleaseVO;
import io.knifer.moghostwebui.service.SingleReleaseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 单发布
 *
 * @author Knifer
 * @version 1.0.0
 */
@RestController
@RequestMapping("/single-release")
@AllArgsConstructor
public class SingleReleaseController {

    private final SingleReleaseService service;

    /**
     * 新增单发布
     * @param request 请求对象
     * @return void
     */
    @PostMapping("/")
    public ApiResult<Void> add(@Valid @RequestBody SingleReleaseAddUpdateRequest request){
        service.add(request);

        return ApiResult.ok();
    }

    /**
     * 根据ID删除
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
     * @param pageParams 分页参数
     * @return data
     */
    @GetMapping("/")
    public ApiResult<Page<SingleReleaseVO>> findPage(PageParams pageParams){
        return ApiResult.ok(service.listPage(pageParams));
    }

    /**
     * 修改
     * @param request 请求对象
     * @return void
     */
    @PatchMapping("/")
    public ApiResult<Void> update(@Valid @RequestBody SingleReleaseAddUpdateRequest request){
        service.update(request);

        return ApiResult.ok();
    }
}

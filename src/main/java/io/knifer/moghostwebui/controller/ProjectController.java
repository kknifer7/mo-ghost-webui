package io.knifer.moghostwebui.controller;

import io.knifer.moghostwebui.common.entity.request.PageParams;
import io.knifer.moghostwebui.common.entity.request.ProjectAddUpdateRequest;
import io.knifer.moghostwebui.common.entity.vo.ApiResult;
import io.knifer.moghostwebui.common.entity.vo.ProjectVO;
import io.knifer.moghostwebui.service.ProjectService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 项目
 *
 * @author Knifer
 * @version 1.0.0
 */
@RestController
@AllArgsConstructor
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService service;

    /**
     * 查询项目列表
     * @param pageParams 分页参数
     * @return 分页数据
     */
    @GetMapping("/")
    public ApiResult<Page<ProjectVO>> getProjectList(PageParams pageParams){
        return ApiResult.ok(service.getProjectList(pageParams.toPageRequest()));
    }

    /**
     * 新增项目
     * @param request 请求参数
     * @return void
     */
    @PostMapping("/")
    public ApiResult<Void> add(@Valid @RequestBody ProjectAddUpdateRequest request){
        service.add(request);

        return ApiResult.ok();
    }

    /**
     * 更新项目
     * @param request 请求参数
     * @return void
     */
    @PatchMapping("/")
    public ApiResult<Void> update(@Valid @RequestBody ProjectAddUpdateRequest request){
        service.update(request);

        return ApiResult.ok();
    }
}

package io.knifer.moghostwebui.controller;

import io.knifer.moghostwebui.common.entity.vo.ApiResult;
import io.knifer.moghostwebui.service.PackageService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 包
 *
 * @author Knifer
 * @version 1.0.0
 */
@RestController
@RequestMapping("/package")
@AllArgsConstructor
public class PackageController {

    private final PackageService service;

    /**
     * 根据ID检查包是否准备完成
     * @param id ID
     * @return bool
     */
    @GetMapping("/ready/{id}")
    public ApiResult<Boolean> checkReady(@PathVariable("id") Integer id){
        return ApiResult.ok(service.checkReadyById(id));
    }

    /**
     * 根据ID删除（要求必须是准备好的包，即ready=true）
     * @param id ID
     * @return void
     */
    @DeleteMapping("/{id}")
    public ApiResult<Void> deleteById(@PathVariable("id") Integer id){
        service.deleteById(id);

        return ApiResult.ok();
    }
}

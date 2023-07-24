package io.knifer.moghostwebui.controller;

import io.knifer.moghostwebui.common.entity.request.UpdateAccountRequest;
import io.knifer.moghostwebui.common.entity.vo.ApiResult;
import io.knifer.moghostwebui.service.SecurityService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 设置控制器
 *
 * @author Knifer
 * @version 1.0.0
 */
@RestController
@RequestMapping("/settings")
@AllArgsConstructor
public class SettingsController {

    private final SecurityService securityService;

    /**
     * 更换账号和密码
     * @param request 请求参数
     * @return void
     */
    @PatchMapping("/account")
    public ApiResult<Void> changeUsernameAndPassword(@Valid @RequestBody UpdateAccountRequest request){
        securityService.updateAccount(request);

        return ApiResult.ok();
    }
}

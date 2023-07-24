package io.knifer.moghostwebui.controller;

import io.knifer.moghostwebui.common.entity.request.LoginRequest;
import io.knifer.moghostwebui.common.entity.vo.ApiResult;
import io.knifer.moghostwebui.service.SecurityService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 *
 * @author Knifer
 * @version 1.0.0
 */
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final SecurityService securityService;

    /**
     * 登录
     * @param request 请求参数
     * @return void
     */
    @PostMapping("/")
    public ApiResult<Void> login(@Valid @RequestBody LoginRequest request){
        securityService.login(request);

        return ApiResult.ok();
    }

    @DeleteMapping("/")
    public ApiResult<Void> logout(){
        securityService.logout();

        return ApiResult.ok();
    }
}

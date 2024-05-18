package io.knifer.moghostwebui.controller;

import io.knifer.moghostwebui.common.entity.request.UpdateAccountRequest;
import io.knifer.moghostwebui.common.entity.vo.ApiResult;
import io.knifer.moghostwebui.common.entity.vo.ValueVO;
import io.knifer.moghostwebui.service.SecurityService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 生成并获取访问密钥
     * @return 访问密钥
     */
    @PostMapping("/access-key")
    public ApiResult<ValueVO<String>> genAccessKey() {
        return ApiResult.ok(ValueVO.from(securityService.genAccessKey()));
    }

    /**
     * 移除访问密钥
     * @return void
     */
    @DeleteMapping("/access-key")
    public ApiResult<Void> deleteAccessKey() {
        securityService.deleteAccessKey();

        return ApiResult.ok();
    }

    /**
     * 查询是否已有访问密钥
     * @return 访问密钥
     */
    @GetMapping("/access-key")
    public ApiResult<ValueVO<Boolean>> hasAccessKey() {
        return ApiResult.ok(ValueVO.from(securityService.hasAccessKey()));
    }
}

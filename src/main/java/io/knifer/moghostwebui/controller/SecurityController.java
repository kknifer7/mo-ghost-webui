package io.knifer.moghostwebui.controller;

import cloud.tianai.captcha.spring.vo.CaptchaResponse;
import cloud.tianai.captcha.spring.vo.ImageCaptchaVO;
import cloud.tianai.captcha.validator.common.model.dto.ImageCaptchaTrack;
import io.knifer.moghostwebui.service.SecurityService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 安全相关控制器
 *
 * @author Knifer
 * @version 1.0.0
 */
@RestController
@RequestMapping("/security")
@AllArgsConstructor
public class SecurityController {

    private final SecurityService service;

    /**
     * 生成验证码
     * @param type 验证码类型
     * @return 验证码数据
     */
    @GetMapping("/gen")
    public CaptchaResponse<ImageCaptchaVO> genCaptcha(@RequestParam(required = false) String type) {
        return service.genCaptcha(type);
    }

    /**
     * 登录（账号密码以请求头形式传递）
     * @param captchaId 验证码ID
     * @param imageCaptchaTrack 验证码轨迹 信息
     * @return void
     */
    @PostMapping("/check")
    public boolean check(
            @RequestParam("id") String captchaId,
            @RequestBody ImageCaptchaTrack imageCaptchaTrack
    ){
        return service.check(captchaId, imageCaptchaTrack);
    }
}

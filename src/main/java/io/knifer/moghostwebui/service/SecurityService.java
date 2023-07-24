package io.knifer.moghostwebui.service;

import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
import cloud.tianai.captcha.spring.application.ImageCaptchaApplication;
import cloud.tianai.captcha.spring.vo.CaptchaResponse;
import cloud.tianai.captcha.spring.vo.ImageCaptchaVO;
import cloud.tianai.captcha.validator.common.model.dto.ImageCaptchaTrack;
import io.knifer.moghostwebui.common.constant.*;
import io.knifer.moghostwebui.common.entity.bo.CaptchaValidationResult;
import io.knifer.moghostwebui.common.entity.domain.SettingEntry;
import io.knifer.moghostwebui.common.entity.request.LoginRequest;
import io.knifer.moghostwebui.common.entity.request.UpdateAccountRequest;
import io.knifer.moghostwebui.common.exception.MoException;
import io.knifer.moghostwebui.common.util.ServletUtil;
import io.knifer.moghostwebui.repository.SettingEntryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Objects;

import static io.knifer.moghostwebui.common.constant.SecurityConstants.CAPTCHA_SESSION_KEY;

/**
 * 安全相关服务
 *
 * @author Knifer
 * @version 1.0.0
 */
@Service
@Slf4j
@AllArgsConstructor
public class SecurityService {

    private final SettingEntryRepository settingEntryRepository;
    private final ImageCaptchaApplication imageCaptchaApplication;

    /**
     * 生成验证码
     * @param type 验证码类型
     * @return 验证码
     */
    public CaptchaResponse<ImageCaptchaVO> genCaptcha(
            @RequestParam(value = "type", required = false) String type
    ) {
        if (StringUtils.isBlank(type)) {
            type = CaptchaTypeConstant.SLIDER;
        }
        ServletUtil.setResponseHeader(SecurityConstants.RSP_HEADER_NO_NEED_RESOLVE, MoConstants.YES_STR);

        return imageCaptchaApplication.generateCaptcha(type);
    }

    /**
     * 登录
     * @param captchaId 验证码ID
     * @param imageCaptchaTrack 验证码轨迹信息
     */
    public boolean check(String captchaId, ImageCaptchaTrack imageCaptchaTrack){
        boolean valid = checkCaptcha(captchaId, imageCaptchaTrack);
        CaptchaValidationResult result = CaptchaValidationResult.of(
                valid,
                LocalDateTime.now().plusSeconds(SecurityConstants.CAPTCHA_EXPIRE_SECONDS)
        );

        if (!valid){
            log.warn("captcha wrong, id={}", captchaId);
        }
        ServletUtil.setSessionAttribute(
                CAPTCHA_SESSION_KEY,
                result
        );
        ServletUtil.setResponseHeader(SecurityConstants.RSP_HEADER_NO_NEED_RESOLVE, MoConstants.YES_STR);


        return valid;
    }

    /**
     * 校验验证码
     * @param id 验证码ID
     * @param imageCaptchaTrack 验证码轨迹数据
     * @return bool
     */
    private boolean checkCaptcha(String id, ImageCaptchaTrack imageCaptchaTrack) {
        return imageCaptchaApplication.matching(id, imageCaptchaTrack).isSuccess();
    }

    /**
     * 登录
     * @param request 请求参数
     */
    public void login(LoginRequest request){
        SettingEntry usernameEntry;
        SettingEntry passwordEntry;
        String username;
        String password;
        CaptchaValidationResult capResult = ServletUtil.getSessionAttribute(
                CAPTCHA_SESSION_KEY, CaptchaValidationResult.class
        );

        ServletUtil.removeSessionAttribute(CAPTCHA_SESSION_KEY);
        if (capResult == null || !capResult.isValid()){
            MoException.throwOut(
                    ErrorCodes.CAPTCHA_WRONG,
                    "login failed, no input captcha or captcha is expired"
            );
        }
        username = request.getUsername();
        password = request.getPassword();
        usernameEntry = getEntryOrThrowExp(SettingConstants.SYS_SEC_USERNAME_ENTRY_KEY);
        passwordEntry = getEntryOrThrowExp(SettingConstants.SYS_SEC_PASSWORD_ENTRY_KEY);
        if (!Objects.equals(username, usernameEntry.getValue()) || !Objects.equals(password, passwordEntry.getValue())){
            MoException.throwOut(
                    ErrorCodes.USERNAME_OR_PASSWORD_WRONG,
                    "login failed, username or password wrong, username=" + username + ", password=" + password
            );
        }
        // 登录成功
        ServletUtil.setSessionAttribute(SecurityConstants.LOGIN_USER_SESSION_KEY, username);
    }

    /**
     * 登出
     */
    public void logout(){
        if (ServletUtil.getSession() != null){
            ServletUtil.removeSessionAttribute(SecurityConstants.LOGIN_USER_SESSION_KEY);
        }
    }

    /**
     * 更新账号密码
     * @param request 请求参数
     */
    public void updateAccount(UpdateAccountRequest request){
        SettingEntry usernameEntry = getEntryOrThrowExp(SettingConstants.SYS_SEC_USERNAME_ENTRY_KEY);
        SettingEntry passwordEntry = getEntryOrThrowExp(SettingConstants.SYS_SEC_PASSWORD_ENTRY_KEY);
        String username = request.getUsername();
        String password = request.getPassword();

        if (!username.equals(usernameEntry.getValue())){
            usernameEntry.setValue(username);
            settingEntryRepository.save(usernameEntry);
        }
        if (!password.equals(passwordEntry.getValue())){
            passwordEntry.setValue(password);
            settingEntryRepository.save(passwordEntry);
        }
    }

    private SettingEntry getEntryOrThrowExp(String key){
        return settingEntryRepository.findByKey(key)
                .orElseThrow(UtilConstants.NULL_OBJ_SUPPLIER);
    }
}

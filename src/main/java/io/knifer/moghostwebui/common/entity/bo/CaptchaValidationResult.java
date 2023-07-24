package io.knifer.moghostwebui.common.entity.bo;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 验证码验证结果缓存
 *
 * @author Knifer
 * @version 1.0.0
 */
@Getter
@ToString
public class CaptchaValidationResult {

    /**
     * 验证是否成功
     */
    private boolean valid;

    /**
     * 过期时间
     */
    private LocalDateTime expireAt;

    private CaptchaValidationResult(boolean valid, LocalDateTime expireAt){
        this.valid = valid;
        this.expireAt = expireAt;
    }

    public boolean isValid(){
        return valid && LocalDateTime.now().isBefore(expireAt);
    }

    public static CaptchaValidationResult of(boolean valid, LocalDateTime expireAt){
        return new CaptchaValidationResult(valid, expireAt);
    }
}

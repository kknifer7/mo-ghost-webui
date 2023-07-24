package io.knifer.moghostwebui.common.entity.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 登录请求
 *
 * @author Knifer
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class LoginRequest {

    @NotBlank
    @Length(max = 16)
    private String username;

    @NotBlank
    @Length(max = 32)
    private String password;
}

package io.knifer.moghostwebui.common.entity.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 版本信息更新
 *
 * @author Knifer
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class VersionInfoUpdateRequest {

    @NotBlank
    @Length(max = 16)
    private String name;
}

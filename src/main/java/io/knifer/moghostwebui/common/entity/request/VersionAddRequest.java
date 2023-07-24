package io.knifer.moghostwebui.common.entity.request;

import io.knifer.moghostwebui.common.constant.VersionStatus;
import io.knifer.moghostwebui.common.constant.VersionUpdatingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 版本新增
 *
 * @author Knifer
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class VersionAddRequest {

    @NotNull
    private Integer projectId;

    @NotBlank
    @Length(max = 16)
    private String name;

    @NotNull
    private VersionStatus status;

    @NotNull
    private VersionUpdatingType updatingType;
}

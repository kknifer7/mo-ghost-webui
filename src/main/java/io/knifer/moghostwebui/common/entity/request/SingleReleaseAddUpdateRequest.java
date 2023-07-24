package io.knifer.moghostwebui.common.entity.request;

import io.knifer.moghostwebui.common.constant.SingleReleaseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 单发布新增、修改
 *
 * @author Knifer
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class SingleReleaseAddUpdateRequest {

    private Integer id;

    /**
     * 名称
     */
    @NotBlank
    @Length(max = 64)
    private String fullName;

    /**
     * 文件ID
     */
    @NotNull
    private Integer fileId;

    /**
     * 发布状态
     */
    @NotNull
    private SingleReleaseStatus releaseStatus;

    /**
     * 备注
     */
    @Length(max = 64)
    private String remark;
}

package io.knifer.moghostwebui.common.entity.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.knifer.moghostwebui.common.constant.SingleReleaseCDKStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 单发布新增、修改
 *
 * @author Knifer
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class SingleReleaseCDKAddUpdateRequest {

    /**
     * ID
     */
    private Integer id;

    /**
     * 编号
     */
    @Length(min = 1, max = 36)
    private String code;

    /**
     * 发布对象ID
     */
    @NotNull
    @Size(max = 60)
    private List<Integer> srIds;

    /**
     * 过期时间
     */
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireAt;

    /**
     * 状态
     */
    @NotNull
    private SingleReleaseCDKStatus cdkStatus;

    /**
     * 备注
     */
    @Length(max = 64)
    private String remark;
}

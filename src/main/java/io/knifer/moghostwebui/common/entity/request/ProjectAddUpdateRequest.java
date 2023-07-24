package io.knifer.moghostwebui.common.entity.request;

import io.knifer.moghostwebui.common.entity.domain.key.ID;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 项目新增、修改
 *
 * @author Knifer
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class ProjectAddUpdateRequest implements ID<Integer> {

    private Integer id;

    @NotBlank
    @Length(max = 64)
    private String name;

    @NotBlank
    @Length(max = 64)
    private String remark;
}

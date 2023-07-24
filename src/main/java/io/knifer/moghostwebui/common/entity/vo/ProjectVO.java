package io.knifer.moghostwebui.common.entity.vo;

import com.google.common.base.Strings;
import io.knifer.moghostwebui.common.entity.domain.key.ID;
import io.knifer.moghostwebui.common.entity.domain.Project;
import lombok.*;

/**
 * 项目
 *
 * @author Knifer
 * @version 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProjectVO implements ID<Integer> {

    private Integer id;

    private String name;

    private String remark;

    public static ProjectVO from(Project project){
        ProjectVO result = new ProjectVO();

        result.setId(project.getId());
        result.setName(Strings.nullToEmpty(project.getName()));
        result.setRemark(Strings.nullToEmpty(project.getRemark()));

        return result;
    }
}

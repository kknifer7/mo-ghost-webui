package io.knifer.moghostwebui.common.entity.domain;

import io.knifer.moghostwebui.common.entity.request.ProjectAddUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

/**
 * 项目
 *
 * @author Knifer
 * @version 1.0.0
 */
@Table(name = "project")
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Project extends BaseEntity<Integer>{

    /**
     * 名称
     */
    @Column(nullable = false, unique = true, length = 64)
    private String name;

    /**
     * 备注
     */
    @Column(nullable = false, length = 64)
    @ColumnDefault("''")
    private String remark;

    public static Project from(ProjectAddUpdateRequest request){
        Project project = new Project();

        project.setName(request.getName());
        project.setRemark(request.getRemark());

        return project;
    }

    public static void update(ProjectAddUpdateRequest source, Project target){
        target.setName(source.getName());
        target.setRemark(source.getRemark());
    }
}

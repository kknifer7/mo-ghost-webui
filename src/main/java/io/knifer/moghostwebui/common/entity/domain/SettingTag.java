package io.knifer.moghostwebui.common.entity.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * - 配置标签 -
 *
 * @author Knifer
 * @version 1.0.0
 */
@Table(name = "setting_tag")
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SettingTag extends BaseEntity<Integer>{

    /**
     * 标签名
     */
    @Column(nullable = false, unique = true, length = 16)
    private String name;

    public static SettingTag of(String name){
        SettingTag result = new SettingTag();

        result.setName(name);

        return result;
    }
}

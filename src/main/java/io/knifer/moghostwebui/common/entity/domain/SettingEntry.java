package io.knifer.moghostwebui.common.entity.domain;

import io.knifer.moghostwebui.common.constant.DataType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * - 配置项 -
 *
 * @author Knifer
 * @version 1.0.0
 */
@Table(name = "setting_entry")
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SettingEntry extends BaseEntity<Integer> {

    /**
     * 键
     */
    @Column(nullable = false, unique = true, length = 16)
    private String key;

    /**
     * 数据类型
     */
    @Enumerated
    @Column(nullable = false)
    private DataType dataType;

    /**
     * 值
     */
    @Column(nullable = false, length = 64)
    private String value;

    /**
     * 标签名称
     */
    @Column(nullable = false, length = 16)
    private String tagName;

    public static SettingEntry of(String key, DataType dataType, String value, String tagName){
        SettingEntry result = new SettingEntry();

        result.setKey(key);
        result.setDataType(dataType);
        result.setValue(value);
        result.setTagName(tagName);

        return result;
    }

}

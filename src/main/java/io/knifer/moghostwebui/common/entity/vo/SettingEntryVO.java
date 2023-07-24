package io.knifer.moghostwebui.common.entity.vo;

import io.knifer.moghostwebui.common.entity.domain.SettingEntry;
import io.knifer.moghostwebui.common.entity.domain.key.ID;
import lombok.*;

/**
 * 配置项
 *
 * @author Knifer
 * @version 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SettingEntryVO implements ID<Integer> {

    /**
     * ID
     */
    private Integer id;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 名称
     */
    private String label;

    /**
     * 值
     */
    private String value;

    public static SettingEntryVO from(SettingEntry po){
        SettingEntryVO result = new SettingEntryVO();

        result.setId(po.getId());
        result.setTagName(po.getTagName());
        result.setLabel(po.getKey());
        result.setValue(po.getValue());

        return result;
    }
}

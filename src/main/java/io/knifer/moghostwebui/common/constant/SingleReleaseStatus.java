package io.knifer.moghostwebui.common.constant;

/**
 * 单发布状态
 *
 * @author Knifer
 * @version 1.0.0
 */
public enum SingleReleaseStatus implements OrdinalEnum{

    /**
     * 创建
     */
    CREATED,
    /**
     * 生效
     */
    PUBLISHED,
    /**
     * 禁用
     */
    DISABLED
    ;

    @Override
    public int getOrdinal() {
        return ordinal();
    }
}

package io.knifer.moghostwebui.common.constant;

/**
 * - 版本状态 -
 *
 * @author Knifer
 * @version 1.0.0
 */
public enum VersionStatus implements OrdinalEnum {
    // 已创建
    CREATED,
    // 已发布
    PUBLISHED,
    // 已禁用
    DISABLED,
    // 已废弃
    ABANDON;

    @Override
    public int getOrdinal() {
        return this.ordinal();
    }
}

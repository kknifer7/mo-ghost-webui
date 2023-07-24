package io.knifer.moghostwebui.common.constant;

/**
 * 版本类型
 *
 * @author Knifer
 * @version 1.0.0
 */
public enum VersionUpdatingType implements OrdinalEnum {

    // 全量更新
    FULL,
    // 增量更新
    INCREMENTAL,
    // 通知更新
    NOTIFICATION;

    @Override
    public int getOrdinal() {
        return this.ordinal();
    }
}

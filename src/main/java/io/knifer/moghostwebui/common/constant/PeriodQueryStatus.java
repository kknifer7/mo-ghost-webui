package io.knifer.moghostwebui.common.constant;

/**
 * 有效期状态查询枚举
 *
 * @author Knifer
 * @version 1.0.0
 */
public enum PeriodQueryStatus implements OrdinalEnum{

    // 所有
    ALL,
    // 有效
    ACTIVATED,
    // 过期
    EXPIRED,
    // 即将过期
    ABOUT_TO_EXPIRED
    ;

    @Override
    public int getOrdinal() {
        return ordinal();
    }
}

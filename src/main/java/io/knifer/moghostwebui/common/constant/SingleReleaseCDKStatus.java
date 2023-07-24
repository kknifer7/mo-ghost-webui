package io.knifer.moghostwebui.common.constant;

/**
 * 单发布CDK状态
 *
 * @author Knifer
 * @version 1.0.0
 */
public enum SingleReleaseCDKStatus implements OrdinalEnum{

    // 激活
    ACTIVATED,
    // 封禁
    BANNED
    ;

    @Override
    public int getOrdinal() {
        return this.ordinal();
    }
}

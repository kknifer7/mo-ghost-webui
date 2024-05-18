package io.knifer.moghostwebui.common.constant;

/**
 * 业务类型
 *
 * @author Knifer
 * @version 1.0.0
 */
public enum BusinessType implements OrdinalEnum {

    // 单发布
    SINGLE_RELEASE,
    // 单发布-文件替换
    SINGLE_RELEASE_FILE_REPLACE,
    // 版本发布
    VERSION_RELEASE
    ;

    @Override
    public int getOrdinal() {
        return ordinal();
    }
}

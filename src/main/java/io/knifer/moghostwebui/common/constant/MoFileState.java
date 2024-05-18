package io.knifer.moghostwebui.common.constant;

/**
 * 文件状态
 *
 * @author Knifer
 * @version 1.0.0
 */
public enum MoFileState implements OrdinalEnum {

    NORMAL,
    MERGING,
    MERGE_FAILED
    ;

    @Override
    public int getOrdinal() {
        return ordinal();
    }
}

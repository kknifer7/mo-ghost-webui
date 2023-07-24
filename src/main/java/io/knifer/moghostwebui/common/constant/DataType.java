package io.knifer.moghostwebui.common.constant;

import lombok.AllArgsConstructor;

/**
 * - 数据类型 -
 *
 * @author Knifer
 * @version 1.0.0
 */
@AllArgsConstructor
public enum DataType implements ValueEnum<String>{

    STRING("S"),
    INTEGER("I"),
    DOUBLE("D");

    private final String value;

    @Override
    public String getValue() {
        return value;
    }
}

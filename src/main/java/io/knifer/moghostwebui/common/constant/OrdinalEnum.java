package io.knifer.moghostwebui.common.constant;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 使用原始Ordinal值的枚举
 *
 * @author Knifer
 * @version 1.0.0
 */
@JsonFormat(shape = JsonFormat.Shape.STRING)
public interface OrdinalEnum {

    int getOrdinal();
}

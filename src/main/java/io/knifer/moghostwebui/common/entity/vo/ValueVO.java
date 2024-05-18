package io.knifer.moghostwebui.common.entity.vo;

import lombok.*;

/**
 * 单值返回对象
 *
 * @author Knifer
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ValueVO<T> {

    private T value;

    private final static ValueVO<?> EMPTY = new ValueVO<>();

    public static<T> ValueVO<T> from(T value) {
        return new ValueVO<>(value);
    }
}

package io.knifer.moghostwebui.common.entity.vo;

import io.knifer.moghostwebui.common.constant.ErrorCodes;
import lombok.*;

/**
 * 接口返回结果
 *
 * @see io.knifer.moghostwebui.common.constant.ErrorCodes
 * @author Knifer
 * @version 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ApiResult<T> {

    private Integer code;

    private T data;

    public static<T> ApiResult<T> ok(){
        return ok(null);
    }

    public static<T> ApiResult<T> ok(T data){
        return of(ErrorCodes.OK, data);
    }

    public static<T> ApiResult<T> fail(Integer code){
        return of(code, null);
    }

    public static<T> ApiResult<T> of(Integer code, T data){
        return new ApiResult<>(code, data);
    }
}

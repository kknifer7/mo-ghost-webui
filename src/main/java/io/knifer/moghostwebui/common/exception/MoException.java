package io.knifer.moghostwebui.common.exception;

/**
 * - 业务异常 -
 *
 * @author Knifer
 * @version 1.0.0
 */
public class MoException extends RuntimeException {

    private Integer code;

    public MoException(Integer code) {
        super();
        this.code = code;
    }

    public MoException(String message) {
        super(message);
    }

    public MoException(Throwable cause) {
        super(cause);
    }

    public MoException(Integer code, String message){
        super(message);
        this.code = code;
    }

    public MoException(Integer code, Throwable cause){
        super(cause);
        this.code = code;
    }

    public void throwOut(){
        throw this;
    }

    public static void throwOut(Integer code){
        new MoException(code).throwOut();
    }

    public static void throwOut(Integer code, String message){
        new MoException(code, message).throwOut();
    }

    public static void throwOut(Integer code, Throwable cause){
        new MoException(code, cause).throwOut();
    }

    public static MoException just(Integer code, String message){
        return new MoException(code, message);
    }

    public Integer getCode(){
        return code;
    }
}

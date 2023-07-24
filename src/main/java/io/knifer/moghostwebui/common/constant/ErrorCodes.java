package io.knifer.moghostwebui.common.constant;

/**
 * - 错误码 -
 *
 * @author Knifer
 * @version 1.0.0
 */
public final class ErrorCodes {

    private ErrorCodes(){
        throw new AssertionError();
    }

    public static Integer OK = 200;

    public static Integer VALIDATION_FAILED = 400;

    public static Integer VALIDATION_DUPLICATED = 401;

    public static Integer FORBIDDEN = 403;

    public static Integer VALIDATION_METHOD_NOT_ALLOWED = 405;

    public static Integer UNKNOWN = 500;

    public static Integer SECURITY_CHECK_FAILED = 1;

    // 文件删除失败，因为有针对该文件的单发布
    public static Integer MO_FILE_HAS_SINGLE_RELEASE = 1001;

    // 账号或密码错误
    public static Integer USERNAME_OR_PASSWORD_WRONG = 4001;

    // （验证码）验证失败
    public static Integer CAPTCHA_WRONG = 4002;

    // 标识符（访问控制CDK）已存在，请更换
    public static Integer SINGLE_RELEASE_CDK_CODE_DUPLICATED = 4003;
}

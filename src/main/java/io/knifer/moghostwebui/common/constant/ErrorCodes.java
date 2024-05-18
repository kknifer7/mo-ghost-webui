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

    public static int OK = 200;

    public static int VALIDATION_FAILED = 400;

    public static int VALIDATION_DUPLICATED = 401;

    public static int FORBIDDEN = 403;

    public static int VALIDATION_METHOD_NOT_ALLOWED = 405;

    public static int UNKNOWN = 500;

    public static int SECURITY_CHECK_FAILED = 1;

    // 文件删除失败，因为有针对该文件的单发布
    public static int MO_FILE_HAS_SINGLE_RELEASE = 1001;

    // 账号或密码错误
    public static int USERNAME_OR_PASSWORD_WRONG = 4001;

    // （验证码）验证失败
    public static int CAPTCHA_WRONG = 4002;

    // 标识符（访问控制CDK）已存在，请更换
    public static int SINGLE_RELEASE_CDK_CODE_DUPLICATED = 4003;
}

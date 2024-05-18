package io.knifer.moghostwebui.common.constant;

/**
 * 安全相关常量
 *
 * @author Knifer
 * @version 1.0.0
 */
public final class SecurityConstants {

    private SecurityConstants(){
        throw new AssertionError();
    }

    public final static String DEFAULT_USERNAME = "admin";

    public final static String DEFAULT_PASSWORD = "moghost123";

    public final static String RSP_HEADER_NO_NEED_RESOLVE = "Mo-Ghost-No-Need-Resolve";

    // 资源获取时的身份验证ID请求头m称
    public final static String REQ_HEADER_MO_GHOST_AUTH_ID = "Mo-Ghost-ID";

    // 高版本安卓的默认无效MAC地址
    public final static String ANDROID_INVALID_MAC = "02:00:00:00:00:00";

    // 验证状态过期时间
    public final static Long CAPTCHA_EXPIRE_SECONDS = 60L;
    // 验证码验证结果存储session key
    public final static String CAPTCHA_SESSION_KEY = "captcha-validation-result";

    // 用户账号Session Key
    public final static String LOGIN_USER_SESSION_KEY = "login_user";

    // 全局认证过滤器排除路径初始化参数名称
    public final static String GLOBAL_AUTH_FILTER_EXCLUDE_URIS_INIT_PARAM_NAME = "exclude-uris";
    // 全局认证过滤器排除路径
    public final static String GLOBAL_AUTH_FILTER_EXCLUDE_URIS =
            "/auth/*,/security/gen,/security/check,/mo-file/single-release/*,/mo-file/access-key/*";
}

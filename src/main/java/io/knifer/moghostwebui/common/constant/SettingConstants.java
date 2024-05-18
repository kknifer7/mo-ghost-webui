package io.knifer.moghostwebui.common.constant;

/**
 * - 配置相关常量 -
 *
 * @author Knifer
 * @version 1.0.0
 */
public final class SettingConstants {

    private SettingConstants(){
        throw new AssertionError();
    }

    public final static String SYS_INIT_TAG_NAME = "sys_init";

    public final static String SYS_INIT_ENTRY_KEY = "sys_initialized";


    /**
     * 系统安全相关配置标签名
     */
    public final static String SYS_SEC_TAG_NAME = "sys_sec";

    /**
     * 系统账号key
     */
    public final static String SYS_SEC_USERNAME_ENTRY_KEY = "admin_name";

    /**
     * 系统密码key
     */
    public final static String SYS_SEC_PASSWORD_ENTRY_KEY = "admin_password";

    /**
     * 系统访问密钥key
     */
    public final static String SYS_SEC_ACCESS_KEY_ENTRY_KEY = "access_key";
}

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


    public final static String SYS_SEC_TAG_NAME = "sys_sec";

    public final static String SYS_SEC_USERNAME_ENTRY_KEY = "admin_name";

    public final static String SYS_SEC_PASSWORD_ENTRY_KEY = "admin_password";
}

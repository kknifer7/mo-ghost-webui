package io.knifer.moghostwebui.common.constant;

/**
 * 常量
 *
 * @author Knifer
 * @version 1.0.0
 */
public final class MoConstants {

    private MoConstants(){
        throw new AssertionError();
    }

    /**
     * 版本目录的名称（前面要拼接storagePath）
     * @see io.knifer.moghostwebui.config.properties.MoGhostProperties.StorageProperties
     */
    public final static String VERSION_DIRECTORY_NAME = "version";

    /**
     * 游离文件目录的名称（前面要拼接storagePath）
     * @see io.knifer.moghostwebui.config.properties.MoGhostProperties.StorageProperties
     */
    public final static String FREE_FILE_DIRECTORY_NAME = "free-file";

    /**
     * 包文件目录的名称（前面要拼接storagePath）
     * @see io.knifer.moghostwebui.config.properties.MoGhostProperties.StorageProperties
     */
    public final static String PACKAGE_DIRECTORY_NAME = "pak";

    public final static String DEFAULT = "DEFAULT";

    public final static String IP_2_REGION_XDB_PATH = "/network/ip2region.xdb";

    public final static String YES_STR = "1";
}

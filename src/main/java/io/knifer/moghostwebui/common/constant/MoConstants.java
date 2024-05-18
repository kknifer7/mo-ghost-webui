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

    /**
     * 暂存区目录的名称（前面要拼接storagePath）
     */
    public final static String WORKING_STORAGE_DIRECTORY_NAME = "working";

    public final static String DEFAULT = "DEFAULT";

    /**
     * IP定位库文件的存放位置
     */
    public final static String IP_2_REGION_XDB_PATH = "/network/ip2region.xdb";

    public final static String YES_STR = "1";

    /**
     * 默认排序值
     */
    public final static Integer DEFAULT_SORT_ORDER = 999;
}

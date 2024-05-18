package io.knifer.moghostwebui.common.util;

import java.util.regex.Pattern;

/**
 * 文件工具类
 *
 * @author Knifer
 * @version 1.0.0
 */
public final class FileUtil {

    private FileUtil() {
        throw new AssertionError();
    }

    private static final Pattern SAFE_FILE_NAME_REGEX = Pattern.compile("^[^\\\\/:*?\"<>|]+");

    /**
     * 验证文件名是否安全
     * @param fileName 文件名称
     * @return bool
     */
    public static boolean isFileNameSafe(String fileName) {
        return fileName != null && SAFE_FILE_NAME_REGEX.matcher(fileName).matches();
    }
}

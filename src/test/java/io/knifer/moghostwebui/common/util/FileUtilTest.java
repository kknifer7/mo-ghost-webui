package io.knifer.moghostwebui.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 文件工具类测试
 *
 * @author Knifer
 * @version 1.0.0
 */
public class FileUtilTest {

    @Test
    void test() {
        assertAll(
                () -> assertTrue(FileUtil.isFileNameSafe("example_file123.txt")),
                () -> assertTrue(FileUtil.isFileNameSafe("file-with-dashes.txt")),
                () -> assertTrue(FileUtil.isFileNameSafe("file_with_underscores.txt")),
                () -> assertTrue(FileUtil.isFileNameSafe("file with spaces.txt")),
                () -> assertTrue(FileUtil.isFileNameSafe("file(with)parentheses.txt")),
                () -> assertTrue(FileUtil.isFileNameSafe("“你好（：）【】{}‘’[]”.txt")),

                () -> assertFalse(FileUtil.isFileNameSafe("")),
                () -> assertFalse(FileUtil.isFileNameSafe("file/with/slashes.txt")),
                () -> assertFalse(FileUtil.isFileNameSafe("file:with:colons.txt")),
                () -> assertFalse(FileUtil.isFileNameSafe("file*with*asterisks.txt")),
                () -> assertFalse(FileUtil.isFileNameSafe("file?with?questionMarks.txt")),
                () -> assertFalse(FileUtil.isFileNameSafe("file\"with\"quotes.txt")),
                () -> assertFalse(FileUtil.isFileNameSafe("file<with<less.than.txt")),
                () -> assertFalse(FileUtil.isFileNameSafe("file>with>greater.than.txt")),
                () -> assertFalse(FileUtil.isFileNameSafe("file|with|pipes.txt")),
                () -> assertFalse(FileUtil.isFileNameSafe("../../../etc/passwd")),
                () -> assertFalse(FileUtil.isFileNameSafe("<script>alert('XSS')</script>"))
        );
    }
}

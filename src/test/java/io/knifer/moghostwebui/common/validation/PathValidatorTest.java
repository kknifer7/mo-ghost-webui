package io.knifer.moghostwebui.common.validation;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 路径校验器使用的正则测试
 *
 * @author Knifer
 * @version 1.0.0
 */
public class PathValidatorTest {

    @Test
    void test(){
        final Pattern PATTERN = Pattern.compile(
                "(/.*|[a-zA-Z]\\\\(([^<>:\"/\\\\|?*!]*[^<>:\"/\\\\|?*.!]\\\\|..\\\\)*([^<>:\"/\\\\|?*.!]*[^<>:\"/\\\\|?*.!]\\\\?|..\\\\))?)"
        );
        List<String> filePaths = new ArrayList<>();

        // 合法路径
        filePaths.add("/path/to/file.txt");
        filePaths.add("/a/b/c");
        filePaths.add("C:\\Users\\User1\\Documents");
        filePaths.add("/downloads/file with spaces.txt");
        filePaths.add("/special_char/$file^name&.txt");
        filePaths.add("/中文路径/文件.txt");

        // 非法路径
        filePaths.add("/path/with:colon/file.txt");
        filePaths.add("/path/with*asterisk/file.txt");
        filePaths.add("/path/with\"double-quotes/file.txt");
        filePaths.add("/path/with<angle-brackets/file.txt");
        filePaths.add("/path/with|pipe/file.txt");
        filePaths.add("/path/with?question-mark/file.txt");
        filePaths.add("/path/with>greater-than/file.txt");
        filePaths.add("/path/with\\backslash/file.txt");

        // 包含特殊字符的路径
        filePaths.add("/path/with!exclamation-mark/file.txt");
        filePaths.add("/path/with#hash/file.txt");
        filePaths.add("/path/with%percent/file.txt");
        filePaths.add("/path/with&ampersand/file.txt");
        filePaths.add("/path/with(single)brackets/file.txt");
        filePaths.add("/path/with/file.txt/");

        // 包含特殊字符的中文路径
        filePaths.add("/中文路径/包含！感叹号/文件.txt");
        filePaths.add("/中文路径/包含#井号/文件.txt");
        filePaths.add("/中文路径/包含%百分号/文件.txt");
        filePaths.add("/中文路径/包含&和/符号/文件.txt");
        filePaths.add("/中文路径/包含(单)括号/文件.txt");

        // 包含特殊字符的Windows路径
        filePaths.add("C:\\path\\with!exclamation-mark\\file.txt");
        filePaths.add("C:\\path\\with#hash\\file.txt");
        filePaths.add("C:\\path\\with%percent\\file.txt");
        filePaths.add("C:\\path\\with&ampersand\\file.txt");
        filePaths.add("C:\\path\\with(single)brackets\\file.txt");

        for (String path : filePaths) {
            System.out.println( path + " : " + PATTERN.matcher(path).matches());
        }
    }
}

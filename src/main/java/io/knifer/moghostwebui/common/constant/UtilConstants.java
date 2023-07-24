package io.knifer.moghostwebui.common.constant;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import io.knifer.moghostwebui.common.exception.MoException;
import org.springframework.data.domain.ExampleMatcher;

import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

/**
 * - 工具常量 -
 *
 * @author Knifer
 * @version 1.0.0
 */
public final class UtilConstants {

    private UtilConstants(){
        throw new AssertionError();
    }

    public static final MoException NULL_OBJ_EXP = new MoException(ErrorCodes.UNKNOWN);

    public static final Supplier<MoException> NULL_OBJ_SUPPLIER = () -> NULL_OBJ_EXP;

    // 查询Example构建
    public static final ExampleMatcher.MatcherConfigurer<ExampleMatcher.GenericPropertyMatcher> EXP_STR_MATCHER =
            m -> m.stringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

    // 逗号Splitter
    public static final Splitter COMMA_SPLITTER = Splitter.on(',').omitEmptyStrings().trimResults();
    // 竖线Splitter
    public static final Splitter PIPE_SPLITTER = Splitter.on('|').omitEmptyStrings().trimResults();
    // 逗号Joiner
    public static final Joiner COMMA_JOINER = Joiner.on(',').skipNulls();

    // 时间格式化-文件存储名称
    public static final DateTimeFormatter STORAGE_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
}

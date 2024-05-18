package io.knifer.moghostwebui.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.StreamReadConstraints;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson配置
 *
 * @author Knifer
 * @version 1.0.0
 */
@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer objectMapperBuilderCustomizer() {
        // 自定义Jackson配置来将最大字符串长度限制设置大一些，避免接收文件分片时JSON解析错误
        return builder -> {
            builder.factory(
                    JsonFactory.builder().streamReadConstraints(
                            StreamReadConstraints.builder().maxStringLength(700_000_000).build()
                    ).build()
            );
        };
    }
}

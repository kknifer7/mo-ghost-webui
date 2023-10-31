package io.knifer.moghostwebui.config;

import io.knifer.moghostwebui.common.constant.SecurityConstants;
import io.knifer.moghostwebui.common.filter.GlobalAuthFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MVC配置
 *
 * @author Knifer
 * @version 1.0.0
 */
@Configuration
public class MvcConfig {

    @Bean
    @ConditionalOnProperty(prefix = "mo-ghost.debug", name = "disable-auth", havingValue = "false")
    public FilterRegistrationBean<GlobalAuthFilter> globalAuthFilter(){
        FilterRegistrationBean<GlobalAuthFilter> filter = new FilterRegistrationBean<>();

        filter.setFilter(new GlobalAuthFilter());
        filter.setOrder(1);
        filter.addUrlPatterns("/*");
        filter.addInitParameter(
                SecurityConstants.GLOBAL_AUTH_FILTER_EXCLUDE_URIS_INIT_PARAM_NAME,
                SecurityConstants.GLOBAL_AUTH_FILTER_EXCLUDE_URIS
        );

        return filter;
    }
}

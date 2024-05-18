package io.knifer.moghostwebui.config;

import io.knifer.moghostwebui.config.properties.MoGhostProperties;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 *
 * @author Knifer
 * @version 1.0.0
 */
@Slf4j
@AllArgsConstructor
@Configuration
@EnableConfigurationProperties({
        MoGhostProperties.class,
        MoGhostProperties.TaskExecutorProperties.class
})
public class TaskExecutorConfig {

    @Bean
    @Resource
    public Executor asyncServiceExecutor(MoGhostProperties.TaskExecutorProperties prop) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(prop.getCorePoolSize());
        executor.setMaxPoolSize(prop.getMaxPoolSize());
        executor.setQueueCapacity(prop.getQueueCapacity());
        executor.setKeepAliveSeconds(prop.getKeepAliveSeconds());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setAllowCoreThreadTimeOut(prop.getAllowCoreThreadTimeOut());
        executor.setPrestartAllCoreThreads(prop.getPreStartAllCoreThreads());
        executor.setThreadNamePrefix(prop.getThreadNamePrefix());
        executor.initialize();
        log.info("asyncServiceExecutor initialized");

        return executor;
    }
}

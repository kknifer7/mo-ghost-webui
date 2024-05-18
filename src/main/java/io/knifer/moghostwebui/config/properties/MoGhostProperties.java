package io.knifer.moghostwebui.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * MoGhost配置项
 *
 * @author Knifer
 * @version 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties("mo-ghost")
public class MoGhostProperties {

    private StorageProperties storage;

    private DebugProperties debug;

    private TaskExecutorProperties taskExecutor;

    @Getter
    @Setter
    @ConfigurationProperties("mo-ghost.storage")
    public static class StorageProperties {

        /**
         * 文件存储路径
         */
        private String savePath = "";

        /**
         * 临时路径（打包时临时存储文件）
         */
        private String tmpPath = "";

        public String getSavePath(){
            if (!savePath.endsWith("/")){
                savePath += "/";
            }

            return savePath;
        }

        public String getTmpPath(){
            if (!tmpPath.endsWith("/")){
                tmpPath += "/";
            }

            return tmpPath;
        }
    }

    @Getter
    @Setter
    @ConfigurationProperties("mo-ghost.debug")
    public static class DebugProperties {

        /**
         * 禁用认证
         */
        private boolean disableAuth = false;
    }

    @Getter
    @Setter
    @ConfigurationProperties("mo-ghost.task-executor")
    public static class TaskExecutorProperties {

        /**
         * 核心线程数
         */
        private Integer corePoolSize = 2;

        /**
         * 最大线程数
         */
        private Integer maxPoolSize = 4;

        /**
         * 队列容量
         */
        private Integer queueCapacity = Integer.MAX_VALUE;

        /**
         * 最大存活时间
         */
        private Integer keepAliveSeconds = 60;

        /**
         * 是否允许核心线程超时关闭
         */
        private Boolean allowCoreThreadTimeOut = false;

        /**
         * 是否预启动所有核心线程
         */
        private Boolean preStartAllCoreThreads = false;

        /**
         * 线程池名称前缀
         */
        private String threadNamePrefix = "mo-ghost-task-executor-";
    }
}

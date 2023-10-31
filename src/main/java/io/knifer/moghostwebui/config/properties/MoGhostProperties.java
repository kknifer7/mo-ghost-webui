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
}

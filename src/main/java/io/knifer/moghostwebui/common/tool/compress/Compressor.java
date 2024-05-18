package io.knifer.moghostwebui.common.tool.compress;

import jakarta.annotation.Nonnull;

/**
 * 压缩
 *
 * @author Knifer
 * @version 1.0.0
 */
public interface Compressor {

    /**
     * 压缩
     * @param inputPath 输入目录路径
     * @param outputPath 输出压缩包路径
     */
    void compress(@Nonnull String inputPath, @Nonnull String outputPath);

}

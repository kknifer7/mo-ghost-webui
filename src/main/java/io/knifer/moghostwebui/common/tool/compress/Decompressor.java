package io.knifer.moghostwebui.common.tool.compress;

import jakarta.annotation.Nonnull;

/**
 * 解压
 *
 * @author Knifer
 * @version 1.0.0
 */
public interface Decompressor {

    /**
     * 解压
     * @param inputPath 输入压缩包路径
     * @param outputPath 输出文件路径
     */
    void decompress(@Nonnull String inputPath, @Nonnull String outputPath);
}

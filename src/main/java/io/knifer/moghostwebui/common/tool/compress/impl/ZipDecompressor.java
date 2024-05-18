package io.knifer.moghostwebui.common.tool.compress.impl;

import io.knifer.moghostwebui.common.constant.ErrorCodes;
import io.knifer.moghostwebui.common.exception.MoException;
import io.knifer.moghostwebui.common.tool.compress.Decompressor;
import io.knifer.moghostwebui.common.tool.security.PathChecker;
import io.knifer.moghostwebui.common.util.RandomUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.tika.Tika;
import org.apache.tika.mime.MediaType;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * ZIP解压
 *
 * @author Knifer
 * @version 1.0.0
 */
@Slf4j
@AllArgsConstructor
@Component
public class ZipDecompressor implements Decompressor {

    private final PathChecker pathChecker;

    @Override
    public void decompress(@NotNull String inputPath, @NotNull String outputPath) {
        decompress(validateAndGetFiles(inputPath, outputPath));
    }

    /**
     * 验证输入、输出路径，返回输入、输出文件对象
     * @param inputPath 输入路径
     * @param outputPath 输出路径
     * @return 输入、输出文件对象
     */
    private Pair<File, File> validateAndGetFiles(String inputPath, String outputPath){
        File zipPath, output;

        // 入参校验、变量初始化
        if (StringUtils.isBlank(inputPath)){
            MoException.throwOut(ErrorCodes.UNKNOWN, "Compress failed, inputPath can't be blank.");
        }
        if (StringUtils.isBlank(outputPath)){
            MoException.throwOut(ErrorCodes.UNKNOWN, "Compress failed, outputPath can't be blank.");
        }
        pathChecker.validate(inputPath);
        pathChecker.validate(outputPath);
        zipPath = Paths.get(inputPath).toFile();
        try {
            if (
                    !zipPath.exists() ||
                    zipPath.isDirectory() ||
                    MediaType.parse(new Tika().detect(zipPath)) != MediaType.APPLICATION_ZIP
            ){
                MoException.throwOut(ErrorCodes.UNKNOWN, "Compress failed, parent is not zip.");
            }
        } catch (IOException e) {
            log.error("decompress validation failed, unknown error", e);
            MoException.throwOut(ErrorCodes.UNKNOWN, "Compress failed, unknown error.");
        }
        output = Paths.get(outputPath, RandomUtil.nextUUID()).toFile();

        return Pair.of(zipPath, output);
    }

    @SuppressWarnings("all")
    private void decompress(Pair<File, File> pathPair){
        byte[] buffer = new byte[1024];
        ArchiveEntry entry;
        File zipFile = pathPair.getFirst(), output = pathPair.getSecond(), file, parent;

        try (ZipArchiveInputStream zis = new ZipArchiveInputStream(new FileInputStream(zipFile))) {
            entry = zis.getNextEntry();
            while (entry != null) {
                file = new File(output, entry.getName());
                if (entry.isDirectory()) {
                    file.mkdirs();
                } else {
                    parent = file.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                entry = zis.getNextEntry();
            }
        } catch (IOException e) {
            log.error("decompress failed, unknown error", e);
            MoException.throwOut(ErrorCodes.UNKNOWN, "decompress failed, unknown error.");
        }
    }
}

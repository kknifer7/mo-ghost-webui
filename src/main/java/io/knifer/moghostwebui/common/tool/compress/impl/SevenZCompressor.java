package io.knifer.moghostwebui.common.tool.compress.impl;

import com.google.common.io.ByteProcessor;
import com.google.common.io.Files;
import io.knifer.moghostwebui.common.constant.ErrorCodes;
import io.knifer.moghostwebui.common.exception.MoException;
import io.knifer.moghostwebui.common.tool.compress.Compressor;
import io.knifer.moghostwebui.common.tool.security.PathChecker;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * 7z解压缩
 *
 * @author Knifer
 * @version 1.0.0
 */
@Component
@AllArgsConstructor
public class SevenZCompressor implements Compressor {

    private final PathChecker pathChecker;

    @Override
    @SuppressWarnings("all")
    public void compress(String inputPath, String outputPath) {
        File parentDir, output;
        long now;

        // 入参校验、变量初始化
        if (StringUtils.isBlank(inputPath)){
            MoException.throwOut(ErrorCodes.UNKNOWN, "Compress failed, parentPath can't be blank.");
        }
        if (StringUtils.isBlank(outputPath)){
            MoException.throwOut(ErrorCodes.UNKNOWN, "Compress failed, outputPath can't be blank.");
        }
        pathChecker.validate(inputPath);
        pathChecker.validate(outputPath);
        parentDir = Paths.get(inputPath).toFile();
        if (!parentDir.exists() || !parentDir.isDirectory()){
            MoException.throwOut(ErrorCodes.UNKNOWN, "Compress failed, parent is not a directory.");
        }
        output = Paths.get(outputPath).toFile();
        if (output.exists()){
            MoException.throwOut(ErrorCodes.UNKNOWN, "Compress failed, archive exists.");
        }
        now = System.currentTimeMillis();
        // 开始压缩
        try (SevenZOutputFile outArchive = new SevenZOutputFile(output)){
            Files.fileTraverser().depthFirstPreOrder(parentDir).forEach(f -> {
                // 创建一个新的文件entry
                SevenZArchiveEntry entry;
                String filePath = f.getPath();

                if (inputPath.equals(filePath)){
                    return;
                }
                entry = new SevenZArchiveEntry();
                entry.setDirectory(f.isDirectory());
                entry.setName(f.getPath().replace(inputPath + File.separator, "").replace(File.separatorChar, '/'));
                entry.setCreationDate(now);
                entry.setLastModifiedDate(now);
                outArchive.putArchiveEntry(entry);
                try {
                    // 如果不是空目录，需要把文件的内容写入
                    if (!entry.isDirectory()){
                        Files.asByteSource(f)
                                .read(new ByteProcessor<byte[]>() {
                                    @Override
                                    public boolean processBytes(@Nullable byte[] buf, int off, int len) throws IOException {
                                        if (buf == null || len == 0){
                                            return false;
                                        }
                                        outArchive.write(buf, off, len);

                                        return true;
                                    }

                                    @Override
                                    public byte[] getResult() {
                                        return null;
                                    }
                                });
                    }
                    outArchive.closeArchiveEntry();
                } catch (IOException e) {
                    MoException.throwOut(ErrorCodes.UNKNOWN, e);
                }
            });
            outArchive.finish();
        } catch (IOException e) {
            MoException.throwOut(ErrorCodes.UNKNOWN, e);
        }
    }
}

package io.knifer.moghostwebui.common.tool.file.impl;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import io.knifer.moghostwebui.common.entity.domain.MoFile;
import io.knifer.moghostwebui.common.entity.domain.Package;
import io.knifer.moghostwebui.common.tool.compress.impl.SevenZCompressor;
import io.knifer.moghostwebui.common.tool.file.FileAssessor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * 文件访问器
 *
 * @author Knifer
 * @version 1.0.0
 */
@Component
@AllArgsConstructor
@SuppressWarnings("all")
public class CommonFileAssessor implements FileAssessor {

    private final SevenZCompressor compressor;

    @Override
    public List<File> getFileList(String inputPath) {
        File file = new File(inputPath);

        if (!file.exists()){
            return List.of();
        }

        return Lists.newArrayList(Files.fileTraverser().depthFirstPreOrder(file));
    }

    @Override
    public void deleteFile(MoFile file) {
        file.toFile().delete();
    }

    @Override
    public void deleteAllFile(Collection<MoFile> files) {
        files.forEach(this::deleteFile);
    }

    @Override
    public void deletePackage(Package p) {
        if (p.getReady()){
            p.toFile().delete();
        }
    }

    @Override
    public void compress(String inputPath, String outputPath) {
        compressor.compress(inputPath, outputPath);
    }
}

package io.knifer.moghostwebui.common.tool.file.impl;

import com.google.common.collect.Lists;
import com.google.common.io.ByteSink;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import io.knifer.moghostwebui.common.constant.ErrorCodes;
import io.knifer.moghostwebui.common.entity.domain.MoFile;
import io.knifer.moghostwebui.common.entity.domain.Package;
import io.knifer.moghostwebui.common.entity.domain.UploadSharding;
import io.knifer.moghostwebui.common.exception.MoException;
import io.knifer.moghostwebui.common.tool.compress.Compressor;
import io.knifer.moghostwebui.common.tool.compress.Decompressor;
import io.knifer.moghostwebui.common.tool.file.FileAssessor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

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

    private final Compressor compressor;
    private final Decompressor decompressor;

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

    @Override
    public void decompress(String inputPath, String outputPath) {
        decompressor.decompress(inputPath, outputPath);
    }

    @Override
    public void saveBytes(byte[] bytes, String savePath) {
        File path = new File(savePath);
        byte[] saveBytes;

        try {
            Files.createParentDirs(path);
            Files.write(bytes, path);
        } catch (Exception e) {
            path.delete();
            MoException.throwOut(ErrorCodes.UNKNOWN, e);
        }
    }

    @Override
    public boolean mergeShard(UploadSharding sharding, String savePath) {
        Integer shardIndex = sharding.getShardIndex();
        Integer shardCount = sharding.getShardCount();
        String mergedShardFilePath;
        File mergedShardFile;
        File targetFile;
        List<File> shardFiles;

        if (sharding.getDoneFlag() || shardIndex < shardCount) {
            return false;
        }
        mergedShardFilePath = sharding.getFilePath();
        mergedShardFile = new File(mergedShardFilePath);
        targetFile = new File(savePath);
        shardFiles = Lists.newArrayListWithCapacity(shardCount);
        if (!targetFile.exists()){
            IntStream.range(0, shardCount)
                    .forEach(idx -> {
                        File shardFile = getShardFile(mergedShardFilePath, idx);
                        ByteSink sink;

                        if (!shardFile.isFile()){
                            MoException.throwOut(
                                    ErrorCodes.UNKNOWN,
                                    "shard file not found, path=" + sharding.getFilePath() + ", idx=" + idx
                            );
                        }
                        sink = Files.asByteSink(targetFile, FileWriteMode.APPEND);
                        try (InputStream in = new FileInputStream(shardFile)){
                            sink.writeFrom(in);
                        } catch (Exception e) {
                            shardFile.delete();
                            MoException.throwOut(ErrorCodes.UNKNOWN, e);
                        }
                        shardFiles.add(shardFile);
                    });
        }
        shardFiles.forEach(File::delete);

        return true;
    }

    @Override
    public List<Integer> findMissingShards(UploadSharding sharding) {
        Integer shardCount = sharding.getShardCount();
        List<Integer> result = Lists.newArrayListWithCapacity(shardCount);
        String savePath = sharding.getFilePath();

        IntStream.range(0, shardCount)
               .forEach(idx -> {
                    File shardFile = getShardFile(savePath, idx);

                    if (!shardFile.isFile()){
                        result.add(idx);
                    }
                });

        return result;
    }

    @Override
    public void deleteFile(UploadSharding sharding) {
        String filePath = sharding.getFilePath();

        IntStream.range(0, sharding.getShardCount())
                .forEach(idx -> getShardFile(filePath, idx).delete());
        new File(filePath).delete();
    }

    private File getShardFile(String savePath, int idx){
        return new File(savePath + "." + idx);
    }
}

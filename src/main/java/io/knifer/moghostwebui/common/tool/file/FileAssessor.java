package io.knifer.moghostwebui.common.tool.file;

import io.knifer.moghostwebui.common.entity.domain.MoFile;
import io.knifer.moghostwebui.common.entity.domain.Package;
import io.knifer.moghostwebui.common.entity.domain.UploadSharding;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * 文件访问器
 *
 * @author Knifer
 * @version 1.0.0
 */
public interface FileAssessor {

    /**
     * 获取文件列表
     * @param inputPath 目录路径
     * @return 文件列表
     */
    List<File> getFileList(String inputPath);

    /**
     * 根据MoFile对象删除文件
     * @param file 文件
     */
    void deleteFile(MoFile file);

    /**
     * 根据MoFile列表批量删除文件
     * @param files 文件列表
     */
    void deleteAllFile(Collection<MoFile> files);

    /**
     * 根据Package删除文件
     * @param p package
     */
    void deletePackage(Package p);

    /**
     * 压缩文件
     * @param inputPath 输入路径
     * @param outputPath 输出路径
     */
    void compress(String inputPath, String outputPath);

    /**
     * 解压文件
     * @param inputPath 输入路径
     * @param outputPath 输出路径
     */
    void decompress(String inputPath, String outputPath);

    /**
     * 保存数据
     * @param bytes 数据内容
     * @param savePath 保存位置（一般为临时目录）
     */
    void saveBytes(byte[] bytes, String savePath);

    /**
     * 合并文件分片。如果无需合并，则不做任何操作
     * @param sharding 分片信息对象
     * @param savePath 最终文件保存位置
     * @return 合并完成/未完成
     */
    boolean mergeShard(UploadSharding sharding, String savePath);

    /**
     * 查询缺失的分片
     * @param sharding 分片信息对象
     * @return 缺失的分片
     */
    List<Integer> findMissingShards(UploadSharding sharding);

    /**
     * 根据分片信息删除文件
     * @param sharding 分片信息对象
     */
    void deleteFile(UploadSharding sharding);

}

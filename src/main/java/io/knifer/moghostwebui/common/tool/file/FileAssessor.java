package io.knifer.moghostwebui.common.tool.file;

import io.knifer.moghostwebui.common.entity.domain.MoFile;
import io.knifer.moghostwebui.common.entity.domain.Package;

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

}

package io.knifer.moghostwebui.common.entity.domain;

import io.knifer.moghostwebui.common.constant.ErrorCodes;
import io.knifer.moghostwebui.common.exception.MoException;
import io.knifer.moghostwebui.common.util.CodecUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * - 文件 -
 *
 * @author Knifer
 * @version 1.0.0
 */
@Table(name = "mo_file")
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MoFile extends BaseEntity<Integer>{

    /**
     * 原始文件名
     */
    @Column(nullable = false, length = 64)
    private String originName;

    /**
     * 存储文件名
     */
    @Column(nullable = false, unique = true, length = 64)
    private String saveName;

    /**
     * 路径（包含文件名）
     */
    @Column(nullable = false, length = 512)
    private String path;

    /**
     * 大小
     */
    @Column(nullable = false)
    @ColumnDefault("0")
    private Long size;

    /**
     * 哈希值
     */
    @Column(length = 128)
    @ColumnDefault("''")
    private String hashValue;

    /**
     * 备注
     */
    @Column(nullable = false, length = 64)
    @ColumnDefault("''")
    private String remark;

    public File toFile(){
        return new File(this.getPath());
    }

    public void refresh(){
        File file = toFile();

        if (!file.exists() || !file.isFile()){
            return;
        }
        this.saveName = file.getName();
        this.size = file.length();
        try {
            this.setHashValue(CodecUtil.sha256String(file));
        } catch (IOException
                e) {
            MoException.throwOut(ErrorCodes.UNKNOWN, e);
        }
    }

    public static MoFile of(MultipartFile originFile, File file){
        MoFile result = new MoFile();

        result.setOriginName(originFile.getOriginalFilename());
        result.setPath(file.getAbsolutePath());
        result.refresh();
        result.setRemark(Strings.EMPTY);

        return result;
    }

    public static MoFile of(MoFile oldFile, Path newPath, String newSaveName){
        MoFile result = new MoFile();

        result.setOriginName(oldFile.getOriginName());
        result.setSaveName(newSaveName);
        result.setPath(newPath.toAbsolutePath().toString());
        result.setSize(oldFile.getSize());
        result.setHashValue(oldFile.getHashValue());
        result.setRemark(oldFile.getRemark());

        return result;
    }
}

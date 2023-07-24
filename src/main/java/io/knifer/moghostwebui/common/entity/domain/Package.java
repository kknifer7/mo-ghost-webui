package io.knifer.moghostwebui.common.entity.domain;

import io.knifer.moghostwebui.common.constant.ErrorCodes;
import io.knifer.moghostwebui.common.constant.MoConstants;
import io.knifer.moghostwebui.common.exception.MoException;
import io.knifer.moghostwebui.common.util.CodecUtil;
import io.knifer.moghostwebui.common.util.RandomUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * 输出包
 *
 * @author Knifer
 * @version 1.0.0
 */
@Table(name = "package")
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Package extends BaseEntity<Integer>{

    /**
     * 项目ID
     */
    @Column(nullable = false)
    private Integer projectId;

    /**
     * 版本ID
     */
    @Column(nullable = false)
    private Integer versionId;

    /**
     * 哈希值
     */
    @Column(length = 128)
    @ColumnDefault("''")
    private String hashValue;

    /**
     * 名称
     */
    @Column(length = 64, nullable = false)
    private String name;

    /**
     * 存储路径
     */
    @Column(nullable = false, length = 512)
    private String savePath;

    /**
     * 是否准备完成
     */
    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean ready;

    public static Package of(Version version, String name, final String STORAGE_PATH){
        Package result = new Package();
        Integer versionId = version.getId();
        String savePath = Path.of(
                STORAGE_PATH, MoConstants.PACKAGE_DIRECTORY_NAME, RandomUtil.nextStorageName(name)
        ).toString();

        result.setProjectId(version.getProjectId());
        result.setVersionId(versionId);
        result.setSavePath(savePath);
        result.setName(name);
        result.refresh();

        return result;
    }

    /**
     * 更新哈希值、ready字段
     */
    public void refresh(){
        File file = new File(savePath);

        if (file.exists()){
            try {
                setHashValue(CodecUtil.sha256String(file));
            } catch (IOException e) {
                MoException.throwOut(ErrorCodes.UNKNOWN, e);
            }
            setReady(Boolean.TRUE);
        }else{
            setHashValue("");
            setReady(Boolean.FALSE);
        }
    }

    /**
     * 转为File
     * @return file
     */
    public File toFile(){
        return new File(getSavePath());
    }
}

package io.knifer.moghostwebui.common.entity.domain;

import io.knifer.moghostwebui.common.constant.BusinessType;
import io.knifer.moghostwebui.common.entity.request.UploadShardingRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

/**
 * 分片上传信息
 *
 * @author Knifer
 * @version 1.0.0
 */
@Table(name = "upload_sharding")
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UploadSharding extends BaseEntity<Integer> {

    /**
     * 文件预保存路径
     */
    @Column(nullable = false, length = 512)
    private String filePath;

    /**
     * 文件名称
     */
    @Column(nullable = false, length = 64)
    private String fileName;

    /**
     * 业务类型
     */
    @Column(nullable = false)
    private BusinessType businessType;

    /**
     * 分片索引（从0开始）
     */
    @Column(nullable = false)
    private Integer shardIndex;

    /**
     * 单个分片大小 Byte
     */
    @Column(nullable = false)
    private Integer shardSize;

    /**
     * 分片总数
     */
    @Column(nullable = false)
    private Integer shardCount;

    /**
     * 文件标识
     */
    @Column(nullable = false, length = 32, unique = true)
    private String fileKey;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean doneFlag;

    public static UploadSharding of(
            UploadShardingRequest request,
            String filePath
    ) {
        UploadSharding result = new UploadSharding();
        String fileKey = request.getFileKey();
        String fileName = request.getFileName();
        Integer shardSize = request.getShardSize();

        result.setFilePath(filePath);
        result.setFileName(fileName);
        result.setBusinessType(request.getBusinessType());
        result.setShardIndex(request.getShardIndex());
        result.setFileKey(fileKey);
        result.setShardSize(shardSize);
        result.setShardCount(request.getShardCount());
        result.setDoneFlag(false);

        return result;
    }
}

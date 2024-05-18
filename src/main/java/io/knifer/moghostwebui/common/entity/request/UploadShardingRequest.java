package io.knifer.moghostwebui.common.entity.request;

import io.knifer.moghostwebui.common.constant.BusinessType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

/**
 * 分片上传请求
 *
 * @author Knifer
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class UploadShardingRequest {

    /**
     * 文件ID（文件替换）
     */
    private Integer fileId;

    /**
     * 文件名称
     */
    @NotBlank
    @Length(max = 64)
    private String fileName;

    /**
     * 业务类型
     */
    @NotNull
    private BusinessType businessType;

    /**
     * 分片索引（从0开始）
     */
    @NotNull
    @Min(0)
    private Integer shardIndex;

    /**
     * 分片大小
     */
    @NotNull
    @Min(1)
    private Integer shardSize;

    /**
     * 分片总数
     */
    @NotNull
    @Range(min = 1, max = 256)
    private Integer shardCount;

    /**
     * 文件唯一标识
     */
    @NotBlank
    @Length(min = 32, max = 32)
    private String fileKey;

    /**
     * 文件大小
     */
    @NotNull
    @Min(0)
    private Long fileSize;

    /**
     * 分片数据内容（base64）
     */
    @NotNull
    private String shardContent;
}

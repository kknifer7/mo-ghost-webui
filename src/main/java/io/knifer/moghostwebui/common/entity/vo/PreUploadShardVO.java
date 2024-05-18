package io.knifer.moghostwebui.common.entity.vo;

import lombok.*;

/**
 * 分片文件上传预备对象
 *
 * @author Knifer
 * @version 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PreUploadShardVO {

    private Integer shardSize;

    public static PreUploadShardVO from(Integer shardSize) {
        PreUploadShardVO result = new PreUploadShardVO();

        result.setShardSize(shardSize);

        return result;
    }
}

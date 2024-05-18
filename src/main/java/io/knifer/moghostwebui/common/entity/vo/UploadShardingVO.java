package io.knifer.moghostwebui.common.entity.vo;

import io.knifer.moghostwebui.common.constant.BusinessType;
import io.knifer.moghostwebui.common.entity.domain.UploadSharding;
import io.knifer.moghostwebui.common.entity.domain.key.ID;
import lombok.*;

/**
 * 分片上传信息
 *
 * @author Knifer
 * @version 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UploadShardingVO implements ID<Integer> {

    private Integer id;

    private String fileName;

    private String fileKey;

    private BusinessType businessType;

    private Integer shardIndex;

    private Integer shardCount;

    private Boolean doneFlag;

    public static UploadShardingVO from(UploadSharding sharding) {
        UploadShardingVO vo = new UploadShardingVO();

        vo.setId(sharding.getId());
        vo.setFileName(sharding.getFileName());
        vo.setFileKey(sharding.getFileKey());
        vo.setBusinessType(sharding.getBusinessType());
        vo.setShardIndex(sharding.getShardIndex());
        vo.setShardCount(sharding.getShardCount());
        vo.setDoneFlag(sharding.getDoneFlag());

        return vo;
    }

    @Override
    public Integer getId() {
        return id;
    }
}

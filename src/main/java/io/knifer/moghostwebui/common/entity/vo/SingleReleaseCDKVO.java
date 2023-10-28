package io.knifer.moghostwebui.common.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.Strings;
import io.knifer.moghostwebui.common.constant.SingleReleaseCDKStatus;
import io.knifer.moghostwebui.common.entity.domain.SingleRelease;
import io.knifer.moghostwebui.common.entity.domain.SingleReleaseCDK;
import io.knifer.moghostwebui.common.entity.domain.key.ID;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 单发布CDK
 *
 * @author Knifer
 * @version 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SingleReleaseCDKVO implements ID<Integer> {

    /**
     * ID
     */
    private Integer id;

    /**
     * 访问码
     */
    private String code;

    /**
     * 身份标识
     */
    private String authWord;

    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireAt;

    /**
     * 过期标志
     */
    private Boolean expired;

    /**
     * 上次访问时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastAccessAt;

    /**
     * 上次访问IP
     */
    private String lastAccessIP;

    /**
     * 上次访问区域
     */
    private String lastAccessRegion;

    /**
     * 总访问量
     */
    private Long totalAccess;

    /**
     * 状态
     */
    private SingleReleaseCDKStatus cdkStatus;

    /**
     * 备注
     */
    private String remark;

    /**
     * 发布对象ID
     */
    private Collection<SingleReleaseVO> singleReleases;

    public static SingleReleaseCDKVO from(SingleReleaseCDK po){
        SingleReleaseCDKVO result = new SingleReleaseCDKVO();
        LocalDateTime expireAt = po.getExpireAt();

        result.setId(po.getId());
        result.setCode(po.getCode());
        result.setAuthWord(Strings.nullToEmpty(po.getAuthWord()));
        result.setExpireAt(expireAt);
        result.setExpired(LocalDateTime.now().isAfter(expireAt));
        result.setLastAccessAt(po.getLastAccessAt());
        result.setLastAccessIP(po.getLastAccessIP());
        result.setLastAccessRegion(po.getLastAccessRegion());
        result.setTotalAccess(po.getTotalAccess());
        result.setCdkStatus(po.getCdkStatus());
        result.setRemark(po.getRemark());
        result.setSingleReleases(List.of());

        return result;
    }

    public static SingleReleaseCDKVO of(SingleReleaseCDK po, Collection<SingleRelease> singleReleases){
        SingleReleaseCDKVO result = from(po);

        result.setSingleReleases(SingleReleaseVO.batchFrom(singleReleases));

        return result;
    }
}

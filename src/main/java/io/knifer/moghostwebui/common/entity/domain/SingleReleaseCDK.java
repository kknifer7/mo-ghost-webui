package io.knifer.moghostwebui.common.entity.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.Strings;
import io.knifer.moghostwebui.common.constant.MoConstants;
import io.knifer.moghostwebui.common.constant.SingleReleaseCDKStatus;
import io.knifer.moghostwebui.common.entity.request.SingleReleaseCDKAddUpdateRequest;
import io.knifer.moghostwebui.common.util.RandomUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

/**
 * 单发布CDK
 *
 * @author Knifer
 * @version 1.0.0
 */
@Table(name = "single_release_cdk")
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SingleReleaseCDK extends BaseEntity<Integer> {

    /**
     * CDK码
     */
    @Column(nullable = false, unique = true, length = 36)
    private String code;

    /**
     * 验证标识
     */
    @Column(unique = true, length = 18)
    private String authWord;

    /**
     * 过期时间
     */
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime expireAt;

    /**
     * 上次访问时间
     */
    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime lastAccessAt;

    /**
     * 上次访问IP
     */
    @Column(nullable = false, length = 45)
    @ColumnDefault("''")
    private String lastAccessIP;

    /**
     * 上次访问区域
     */
    @Column(nullable = false, length = 128)
    @ColumnDefault("''")
    private String lastAccessRegion;

    /**
     * 总访问量
     */
    @Column(nullable = false)
    @ColumnDefault("0")
    private Long totalAccess;

    /**
     * 状态
     */
    @Enumerated
    @Column(nullable = false)
    private SingleReleaseCDKStatus cdkStatus;

    /**
     * 备注
     */
    @Column(nullable = false, length = 64)
    @ColumnDefault("''")
    private String remark;

    /**
     * 排序值
     */
    @Column(nullable = false)
    @ColumnDefault("999")
    private Integer sortOrder;

    public static SingleReleaseCDK from(SingleReleaseCDKAddUpdateRequest request){
        SingleReleaseCDK result = new SingleReleaseCDK();

        result.setCode(RandomUtil.nextUUID(true));
        result.setLastAccessIP("");
        result.setLastAccessRegion("");
        result.setTotalAccess(0L);
        result.setExpireAt(request.getExpireAt());
        result.setCdkStatus(request.getCdkStatus());
        result.setRemark(Strings.nullToEmpty(request.getRemark()));
        result.setSortOrder(MoConstants.DEFAULT_SORT_ORDER);

        return result;
    }

    public static void update(SingleReleaseCDKAddUpdateRequest source, SingleReleaseCDK target){
        target.setCode(source.getCode());
        target.setExpireAt(source.getExpireAt());
        target.setCdkStatus(source.getCdkStatus());
        target.setRemark(Strings.nullToEmpty(source.getRemark()));
        target.setSortOrder(source.getSortOrder());
        if (target.getSortOrder() == null) {
            target.setSortOrder(MoConstants.DEFAULT_SORT_ORDER);
        }
    }
}

package io.knifer.moghostwebui.common.entity.domain;

import com.google.common.base.Strings;
import io.knifer.moghostwebui.common.constant.SingleReleaseStatus;
import io.knifer.moghostwebui.common.entity.request.SingleReleaseAddUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

/**
 * 单资源发布
 *
 * @author Knifer
 * @version 1.0.0
 */
@Table(name = "single_release")
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SingleRelease extends BaseEntity<Integer>{

    /**
     * 名称
     */
    @Column(nullable = false, length = 64)
    private String fullName;

    /**
     * 文件ID
     */
    @Column(nullable = false)
    private Integer fileId;

    /**
     * 发布状态
     */
    @Enumerated
    @Column(nullable = false)
    private SingleReleaseStatus releaseStatus;

    /**
     * 总访问量
     */
    @Column(nullable = false)
    @ColumnDefault("0")
    private Long totalAccess;

    /**
     * 备注
     */
    @Column(nullable = false, length = 64)
    @ColumnDefault("''")
    private String remark;

    public static SingleRelease from(SingleReleaseAddUpdateRequest request){
        SingleRelease result = new SingleRelease();

        result.setTotalAccess(0L);
        update(request, result);

        return result;
    }

    public static void update(SingleReleaseAddUpdateRequest source, SingleRelease target){
        target.setFullName(source.getFullName());
        target.setFileId(source.getFileId());
        target.setReleaseStatus(source.getReleaseStatus());
        target.setRemark(Strings.nullToEmpty(source.getRemark()));
    }
}

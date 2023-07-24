package io.knifer.moghostwebui.common.entity.domain;

import io.knifer.moghostwebui.common.constant.MoConstants;
import io.knifer.moghostwebui.common.constant.VersionStatus;
import io.knifer.moghostwebui.common.constant.VersionUpdatingType;
import io.knifer.moghostwebui.common.entity.request.VersionAddRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.nio.file.Path;

/**
 * - 版本 -
 *
 * @author Knifer
 * @version 1.0.0
 */
@Table(name = "version")
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Version extends BaseEntity<Integer> {

    /**
     * 版本ID
     */
    @Column(nullable = false)
    private Integer projectId;

    /**
     * 版本名
     */
    @Column(nullable = false, length = 16)
    private String name;

    /**
     * 版本状态
     */
    @Enumerated
    @Column(nullable = false)
    private VersionStatus status;

    /**
     * 版本更新类型
     */
    @Enumerated
    @Column(nullable = false)
    private VersionUpdatingType updatingType;

    public String createSavePath(final String STORAGE_PATH){
        return Path.of(STORAGE_PATH, MoConstants.VERSION_DIRECTORY_NAME, id.toString()).toString();
    }

    public static Version from(VersionAddRequest request){
        Version version = new Version();

        version.setProjectId(request.getProjectId());
        version.setName(request.getName());
        version.setStatus(request.getStatus());
        version.setUpdatingType(request.getUpdatingType());

        return version;
    }
}

package io.knifer.moghostwebui.common.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.knifer.moghostwebui.common.constant.VersionStatus;
import io.knifer.moghostwebui.common.constant.VersionUpdatingType;
import io.knifer.moghostwebui.common.entity.domain.key.ID;
import io.knifer.moghostwebui.common.entity.domain.Version;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 版本
 *
 * @author Knifer
 * @version 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class VersionVO implements ID<Integer> {

    private Integer id;

    private String name;

    private VersionStatus versionStatus;

    private VersionUpdatingType updatingType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime ctime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime mtime;

    public static VersionVO from(Version version){
        VersionVO result = new VersionVO();

        result.setId(version.getId());
        result.setName(version.getName());
        result.setVersionStatus(version.getStatus());
        result.setUpdatingType(version.getUpdatingType());
        result.setCtime(version.getCtime());
        result.setMtime(version.getMtime());

        return result;
    }
}

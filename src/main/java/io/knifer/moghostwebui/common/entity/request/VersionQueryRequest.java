package io.knifer.moghostwebui.common.entity.request;

import io.knifer.moghostwebui.common.constant.VersionStatus;
import io.knifer.moghostwebui.common.constant.VersionUpdatingType;
import io.knifer.moghostwebui.common.entity.domain.Version;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import static io.knifer.moghostwebui.common.constant.UtilConstants.EXP_STR_MATCHER;

/**
 * 版本查询
 *
 * @author Knifer
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class VersionQueryRequest implements QueryRequest<Version>{

    private Integer projectId;

    @Length(max = 16)
    private String name;

    private VersionStatus status;

    private VersionUpdatingType updatingType;

    @Override
    public Example<Version> toExample() {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", EXP_STR_MATCHER);
        Version probe = Version.builder()
                .projectId(projectId)
                .name(name)
                .status(status)
                .updatingType(updatingType)
                .build();

        return Example.of(probe, matcher);
    }
}

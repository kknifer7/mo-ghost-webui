package io.knifer.moghostwebui.common.entity.request;

import io.knifer.moghostwebui.common.constant.UtilConstants;
import io.knifer.moghostwebui.common.entity.domain.MoFile;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

/**
 * 文件查询
 *
 * @author Knifer
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class MoFileQueryRequest implements QueryRequest<MoFile>{

    @Length(max = 64)
    private String originName;

    @Length(max = 512)
    private String path;

    @Length(max = 64)
    private String remark;

    @Override
    public Example<MoFile> toExample() {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("originName", UtilConstants.EXP_STR_MATCHER)
                .withMatcher("path", UtilConstants.EXP_STR_MATCHER)
                .withMatcher("remark", UtilConstants.EXP_STR_MATCHER);
        MoFile probe = MoFile.builder()
                .originName(originName)
                .path(path)
                .remark(remark)
                .build();

        return Example.of(probe, matcher);
    }
}

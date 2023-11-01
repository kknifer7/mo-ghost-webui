package io.knifer.moghostwebui.common.entity.request;

import io.knifer.moghostwebui.common.constant.CharConstants;
import io.knifer.moghostwebui.common.constant.PeriodQueryStatus;
import io.knifer.moghostwebui.common.constant.SingleReleaseCDKStatus;
import io.knifer.moghostwebui.common.entity.domain.SingleReleaseCDK;
import io.knifer.moghostwebui.common.entity.domain.SingleReleaseCDK_;
import io.knifer.moghostwebui.common.util.SpecUtil;
import jakarta.persistence.criteria.Predicate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

import static io.knifer.moghostwebui.common.constant.UtilConstants.EXP_STR_MATCHER;
import static io.knifer.moghostwebui.common.util.SpecUtil.KeyWord.AND;

/**
 * 单发布CDK查询
 *
 * @author Knifer
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class SingleReleaseCDKQueryRequest implements QueryRequest<SingleReleaseCDK>{

    private PeriodQueryStatus tab;

    private SingleReleaseCDKStatus cdkStatus;

    @Length(max = 64)
    private String remark;

    @Override
    public Example<SingleReleaseCDK> toExample() {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher(SingleReleaseCDK_.REMARK, EXP_STR_MATCHER)
                .withIgnoreCase();
        SingleReleaseCDK probe = SingleReleaseCDK.builder()
                .remark(remark)
                .cdkStatus(cdkStatus)
                .build();


        return Example.of(probe, matcher);
    }

    @Override
    public Specification<SingleReleaseCDK> toSpecification() {
        return (root, query, builder) -> {
            Predicate result = null;

            if (remark != null){
                result = SpecUtil.acceptNonnull(builder, result, AND, builder.like(
                        builder.lower(root.get(SingleReleaseCDK_.REMARK)),
                        StringUtils.wrap(remark.toLowerCase(), CharConstants.PERCENT)
                ));
            }
            if (cdkStatus != null){
                result = SpecUtil.acceptNonnull(builder, result, AND, builder.equal(
                        root.get(SingleReleaseCDK_.CDK_STATUS), cdkStatus
                ));
            }
            if (tab != null){
                result = SpecUtil.acceptNonnull(builder, result, AND, switch (tab) {
                    case ALL -> null;
                    case ACTIVATED -> builder.greaterThanOrEqualTo(
                            root.get(SingleReleaseCDK_.EXPIRE_AT), LocalDate.now()
                    );
                    case EXPIRED -> builder.lessThan(root.get(SingleReleaseCDK_.EXPIRE_AT), LocalDate.now());
                    case ABOUT_TO_EXPIRED -> builder.between(
                            root.get(SingleReleaseCDK_.EXPIRE_AT),
                            LocalDate.now(),
                            LocalDate.now().plusMonths(1)
                    );
                });
            }

            return result;
        };
    }
}

package io.knifer.moghostwebui.common.entity.domain.key;

import jakarta.persistence.Column;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 单发布-单发布CDK 主键
 *
 * @author Knifer
 * @version 1.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class SingleReleaseCDKKey extends ManyToManyKey<Integer, Integer> {

    /**
     * 发布对象ID
     */
    @Column(nullable = false)
    private Integer srId;

    /**
     * cdk ID
     */
    @Column(nullable = false)
    private Integer cdkId;

    @Override
    protected Integer getK1() {
        return srId;
    }

    @Override
    protected Integer getK2() {
        return cdkId;
    }
}

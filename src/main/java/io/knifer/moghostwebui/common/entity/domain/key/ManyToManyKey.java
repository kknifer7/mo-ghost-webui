package io.knifer.moghostwebui.common.entity.domain.key;

import jakarta.persistence.Embeddable;
import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;

/**
 * 多对多关联表主键
 *
 * @author Knifer
 * @version 1.0.0
 */
@Embeddable
@MappedSuperclass
public abstract class ManyToManyKey<K1, K2> implements Serializable {

    abstract protected K1 getK1();

    abstract protected K2 getK2();
}

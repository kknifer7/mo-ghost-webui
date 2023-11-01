package io.knifer.moghostwebui.common.entity.request;

import io.knifer.moghostwebui.common.entity.domain.BaseEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;

/**
 * 查询请求
 *
 * @author Knifer
 * @version 1.0.0
 */
public interface QueryRequest<T extends BaseEntity<?>> {

    default Example<T> toExample() {
        throw new UnsupportedOperationException();
    }

    default Specification<T> toSpecification() {
        throw new UnsupportedOperationException();
    }
}

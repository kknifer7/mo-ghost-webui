package io.knifer.moghostwebui.common.entity.request;

import io.knifer.moghostwebui.common.entity.domain.BaseEntity;
import org.springframework.data.domain.Example;

/**
 * 查询请求
 *
 * @author Knifer
 * @version 1.0.0
 */
public interface QueryRequest<T extends BaseEntity<?>> {

    Example<T> toExample();
}

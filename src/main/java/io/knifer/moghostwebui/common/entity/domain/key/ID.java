package io.knifer.moghostwebui.common.entity.domain.key;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 带有ID的对象
 *
 * @author Knifer
 * @version 1.0.0
 */
public interface ID<T> {

    T getId();

    static <E extends ID<R>, R> Collection<R> mapIds(Collection<E> poList){
        return poList.stream().map(ID::getId).toList();
    }

    static <E extends ID<R>, R> Map<R, E> mapIdsMap(Collection<E> poList){
        return poList.stream().collect(Collectors.toMap(ID::getId, e -> e));
    }
}

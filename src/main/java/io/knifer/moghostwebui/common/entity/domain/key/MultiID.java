package io.knifer.moghostwebui.common.entity.domain.key;

import java.util.List;

/**
 * 多主键
 *
 * @author Knifer
 * @version 1.0.0
 */
public interface MultiID<K1, K2> {

    ManyToManyKey<K1, K2> getId();

    static <K1, K2> List<K1> k1List(List<? extends MultiID<K1, K2>> keys){
        return keys.stream().map(mId -> mId.getId().getK1()).toList();
    }

    static <K1, K2> List<K2> k2List(List<? extends MultiID<K1, K2>> keys){
        return keys.stream().map(mId -> mId.getId().getK2()).toList();
    }
}

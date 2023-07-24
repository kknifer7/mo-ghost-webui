package io.knifer.moghostwebui.common.entity.domain;

import io.knifer.moghostwebui.common.entity.domain.key.MultiID;
import io.knifer.moghostwebui.common.entity.domain.key.SingleReleaseCDKKey;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 单发布-单发布CDK关联
 *
 * @author Knifer
 * @version 1.0.0
 */
@Table(name = "aso_single_release_cdk")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SingleReleaseCDKASO implements MultiID<Integer, Integer> {

    @EmbeddedId
    private SingleReleaseCDKKey id;

    public static SingleReleaseCDKASO of(Integer srId, Integer cdkId){
        SingleReleaseCDKKey id = new SingleReleaseCDKKey();
        SingleReleaseCDKASO result = new SingleReleaseCDKASO();

        id.setSrId(srId);
        id.setCdkId(cdkId);
        result.setId(id);

        return result;
    }

    public static List<SingleReleaseCDKASO> batchOf(List<Integer> srIds, Integer cdkId){
        return srIds.stream()
                .map(srId -> of(srId, cdkId))
                .toList();
    }
}

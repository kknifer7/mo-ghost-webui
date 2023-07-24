package io.knifer.moghostwebui.repository;

import io.knifer.moghostwebui.common.entity.domain.SingleRelease;
import io.knifer.moghostwebui.common.entity.domain.SingleReleaseCDKASO;
import io.knifer.moghostwebui.common.entity.domain.key.SingleReleaseCDKKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Pair;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 单发布-单发布CDK 关联
 *
 * @author Knifer
 * @version 1.0.0
 */
public interface SingleReleaseCDKASORepository extends JpaRepository<SingleReleaseCDKASO, SingleReleaseCDKKey> {

    void deleteByIdSrId(Integer srId);

    void deleteByIdCdkId(Integer cdkId);

    Optional<SingleReleaseCDKASO> findByIdCdkIdAndIdSrId(Integer cdkId, Integer srId);

    @Query("""
        select new org.springframework.data.util.Pair(aso.id.cdkId, sr) from SingleRelease sr
        left join SingleReleaseCDKASO aso on sr.id = aso.id.srId
        where aso.id.cdkId IN ?1
    """)
    List<Pair<Integer, SingleRelease>> findCdkIdAndReleaseByIdCdkIds(Collection<Integer> cdkIds);
}

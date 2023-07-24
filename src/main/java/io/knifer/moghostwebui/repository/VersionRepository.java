package io.knifer.moghostwebui.repository;

import io.knifer.moghostwebui.common.constant.VersionStatus;
import io.knifer.moghostwebui.common.constant.VersionUpdatingType;
import io.knifer.moghostwebui.common.entity.domain.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * - 版本存储 -
 *
 * @author Knifer
 * @version 1.0.0
 */
@Repository
public interface VersionRepository extends JpaRepository<Version, Integer>, QueryByExampleExecutor<Version> {



    boolean existsByNameAndProjectIdAndIdNot(@NonNull String name, @NonNull Integer projectId, @NonNull Integer id);

    @Transactional
    @Modifying
    @Query("update Version v set v.name = ?1 where v.id = ?2")
    void updateNameById(@NonNull String name, @NonNull Integer id);

    @Transactional
    @Modifying
    @Query("update Version v set v.updatingType = ?1 where v.id = ?2")
    void updateUpdatingTypeById(@NonNull VersionUpdatingType updatingType, @NonNull Integer id);

    boolean existsByNameAndProjectId(@NonNull String name, @NonNull Integer projectId);

    @Transactional
    @Modifying
    @Query("update Version v set v.status = ?1 where v.id = ?2")
    void updateStatusById(@NonNull VersionStatus status, @NonNull Integer id);
}

package io.knifer.moghostwebui.repository;

import io.knifer.moghostwebui.common.entity.domain.SingleRelease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * 单发布
 *
 * @author Knifer
 * @version 1.0.0
 */
@Repository
@SuppressWarnings("all")
public interface SingleReleaseRepository extends JpaRepository<SingleRelease, Integer> {
    boolean existsByFileIdIn(@NonNull Collection<Integer> fileIds);
    boolean existsByFileId(@NonNull Integer fileId);

    long countByIdIn(@NonNull Collection<Integer> ids);
}

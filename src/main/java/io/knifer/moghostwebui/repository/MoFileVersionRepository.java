package io.knifer.moghostwebui.repository;

import io.knifer.moghostwebui.common.entity.domain.MoFileVersion;
import io.knifer.moghostwebui.common.entity.domain.key.MoFileVersionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

/**
 * 文件-版本关联
 *
 * @author Knifer
 * @version 1.0.0
 */
@Repository
public interface MoFileVersionRepository extends JpaRepository<MoFileVersion, MoFileVersionKey> {

    boolean existsByIdMoFileIdAndIdVersionId(Integer moFileId, Integer versionId);

    Collection<MoFileVersion> findByIdMoFileIdIn(Collection<Integer> moFileIds);

    @Query("select mf.id.moFileId from MoFileVersion mf where mf.id.versionId = ?1")
    Set<Integer> findIdMoFileIdByIdVersionId(Integer versionId);

    void deleteByIdMoFileId(Integer moFileId);

    void deleteByIdVersionId(Integer versionId);

    void deleteByIdMoFileIdIn(Collection<Integer> moFileIds);
}

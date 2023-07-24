package io.knifer.moghostwebui.repository;

import io.knifer.moghostwebui.common.entity.domain.MoFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * - 文件存储 -
 *
 * @author Knifer
 * @version 1.0.0
 */
@Repository
public interface MoFileRepository extends JpaRepository<MoFile, Integer>, QueryByExampleExecutor<MoFile> {

    @Query("""
        select mf
        from MoFile mf
        left join MoFileVersion mfv on mf.id = mfv.id.moFileId
        where
            mfv.id.versionId = ?1
    """)
    List<MoFile> findByVersionId(Integer versionId);

    @Query("""
        select mf
        from MoFile mf
        where not exists(
            select mfv
            from MoFileVersion mfv
            where
                mf.id = mfv.id.moFileId
        )
    """)
    Page<MoFile> findByVersionIdNotExists(Pageable pageable);
}

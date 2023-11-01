package io.knifer.moghostwebui.repository;

import io.knifer.moghostwebui.common.entity.domain.SingleReleaseCDK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 单发布CDK
 *
 * @author Knifer
 * @version 1.0.0
 */
@Repository
public interface SingleReleaseCDKRepository
        extends JpaRepository<SingleReleaseCDK, Integer>, JpaSpecificationExecutor<SingleReleaseCDK> {

    boolean existsByAuthWord(@NonNull String authWord);

    boolean existsByCodeAndIdNot(@NonNull String code, @NonNull Integer id);

    Optional<SingleReleaseCDK> findByCode(@NonNull String code);
}

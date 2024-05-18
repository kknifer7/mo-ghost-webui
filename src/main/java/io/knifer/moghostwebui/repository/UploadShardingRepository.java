package io.knifer.moghostwebui.repository;

import io.knifer.moghostwebui.common.entity.domain.UploadSharding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 分片上传
 *
 * @author Knifer
 * @version 1.0.0
 */
@Repository
public interface UploadShardingRepository extends JpaRepository<UploadSharding, Integer> {
    void deleteByFileKey(@NonNull String fileKey);

    Optional<UploadSharding> findByFileKey(String fileKey);

    void deleteByDoneFlagTrue();
}

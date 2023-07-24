package io.knifer.moghostwebui.repository;

import io.knifer.moghostwebui.common.entity.domain.SettingTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * - 配置标签存储 -
 *
 * @author Knifer
 * @version 1.0.0
 */
@Repository
public interface SettingTagRepository extends JpaRepository<SettingTag, Integer> {
    long deleteByName(@NonNull String name);

    Optional<SettingTag> findByName(String name);
}

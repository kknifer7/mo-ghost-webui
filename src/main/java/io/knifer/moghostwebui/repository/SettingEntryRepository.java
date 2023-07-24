package io.knifer.moghostwebui.repository;

import io.knifer.moghostwebui.common.entity.domain.SettingEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * - 配置项存储 -
 *
 * @author Knifer
 * @version 1.0.0
 */
@Repository
public interface SettingEntryRepository extends JpaRepository<SettingEntry, Integer> {

    List<SettingEntry> findByTagName(String tagName);

    Optional<SettingEntry> findByKey(String key);

    int deleteByKey(String key);
}

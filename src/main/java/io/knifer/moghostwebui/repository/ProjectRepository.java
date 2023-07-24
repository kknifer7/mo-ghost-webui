package io.knifer.moghostwebui.repository;

import io.knifer.moghostwebui.common.entity.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

/**
 * 项目
 *
 * @author Knifer
 * @version 1.0.0
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    boolean existsByNameAndIdNot(@NonNull String name, @NonNull Integer id);
    boolean existsByName(@NonNull String name);
}

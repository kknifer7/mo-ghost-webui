package io.knifer.moghostwebui.repository;

import io.knifer.moghostwebui.common.entity.domain.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 包
 *
 * @author Knifer
 * @version 1.0.0
 */
@Repository
public interface PackageRepository extends JpaRepository<Package, Integer> {

    Boolean existsByIdAndReadyTrue(Integer id);
}

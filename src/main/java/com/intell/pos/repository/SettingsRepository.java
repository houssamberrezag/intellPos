/**
 *
 */
package com.intell.pos.repository;

import com.intell.pos.domain.Settings;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author HP
 *
 */

@SuppressWarnings("unused")
@Repository
public interface SettingsRepository extends JpaRepository<Settings, Long> {
    Optional<Settings> findFirstByIdIsNotNullOrderByIdDesc();
}

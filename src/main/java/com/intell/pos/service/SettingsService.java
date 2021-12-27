/**
 *
 */
package com.intell.pos.service;

import com.intell.pos.domain.Settings;
import java.util.Optional;

/**
 * @author HP
 *
 */
public interface SettingsService {
    /**
     * Save a settings.
     *
     * @param settngs the entity to save.
     * @return the persisted entity.
     */
    Settings save(Settings settings);

    /**
     * Get the "id" settings.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Settings> findcurrent();
}

package com.intell.pos.service;

import com.intell.pos.domain.Damage;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Damage}.
 */
public interface DamageService {
    /**
     * Save a damage.
     *
     * @param damage the entity to save.
     * @return the persisted entity.
     */
    Damage save(Damage damage);

    /**
     * Partially updates a damage.
     *
     * @param damage the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Damage> partialUpdate(Damage damage);

    /**
     * Get all the damages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Damage> findAll(Pageable pageable);

    /**
     * Get the "id" damage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Damage> findOne(Long id);

    /**
     * Delete the "id" damage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

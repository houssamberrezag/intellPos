package com.intell.pos.service;

import com.intell.pos.domain.Taxe;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Taxe}.
 */
public interface TaxeService {
    /**
     * Save a taxe.
     *
     * @param taxe the entity to save.
     * @return the persisted entity.
     */
    Taxe save(Taxe taxe);

    /**
     * Partially updates a taxe.
     *
     * @param taxe the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Taxe> partialUpdate(Taxe taxe);

    /**
     * Get all the taxes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Taxe> findAll(Pageable pageable);

    /**
     * Get the "id" taxe.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Taxe> findOne(Long id);

    /**
     * Delete the "id" taxe.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

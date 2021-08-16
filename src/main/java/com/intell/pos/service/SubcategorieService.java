package com.intell.pos.service;

import com.intell.pos.domain.Subcategorie;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Subcategorie}.
 */
public interface SubcategorieService {
    /**
     * Save a subcategorie.
     *
     * @param subcategorie the entity to save.
     * @return the persisted entity.
     */
    Subcategorie save(Subcategorie subcategorie);

    /**
     * Partially updates a subcategorie.
     *
     * @param subcategorie the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Subcategorie> partialUpdate(Subcategorie subcategorie);

    /**
     * Get all the subcategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Subcategorie> findAll(Pageable pageable);

    /**
     * Get the "id" subcategorie.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Subcategorie> findOne(Long id);

    /**
     * Delete the "id" subcategorie.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

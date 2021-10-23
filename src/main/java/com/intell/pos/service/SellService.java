package com.intell.pos.service;

import com.intell.pos.domain.Sell;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Sell}.
 */
public interface SellService {
    /**
     * Save a sell.
     *
     * @param sell the entity to save.
     * @return the persisted entity.
     */
    Sell save(Sell sell);

    /**
     * Partially updates a sell.
     *
     * @param sell the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Sell> partialUpdate(Sell sell);

    /**
     * Get all the sells.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Sell> findAll(Pageable pageable);

    /**
     * Get the "id" sell.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Sell> findOne(Long id);

    /**
     * Delete the "id" sell.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<Sell> findByReference(String reference);
}

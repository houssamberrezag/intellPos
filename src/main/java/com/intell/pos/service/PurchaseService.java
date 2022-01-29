package com.intell.pos.service;

import com.intell.pos.domain.Purchase;
import com.intell.pos.domain.projection.ITodaySellsAndItems;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Purchase}.
 */
public interface PurchaseService {
    /**
     * Save a purchase.
     *
     * @param purchase the entity to save.
     * @return the persisted entity.
     */
    Purchase save(Purchase purchase);

    /**
     * Partially updates a purchase.
     *
     * @param purchase the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Purchase> partialUpdate(Purchase purchase);

    /**
     * Get all the purchases.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Purchase> findAll(Pageable pageable);

    /**
     * Get the "id" purchase.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Purchase> findOne(Long id);

    /**
     * Delete the "id" purchase.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get purchases by reference.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    List<Purchase> findByReference(String reference);

    Page<Purchase> findByProductId(Long productId, Pageable pageable);

    int findtotalQuantityByPersonId(Long personId);

    ITodaySellsAndItems findSumQuantitesAndItems(Instant debut, Instant fin);
}

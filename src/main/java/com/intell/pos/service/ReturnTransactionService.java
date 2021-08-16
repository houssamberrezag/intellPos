package com.intell.pos.service;

import com.intell.pos.domain.ReturnTransaction;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ReturnTransaction}.
 */
public interface ReturnTransactionService {
    /**
     * Save a returnTransaction.
     *
     * @param returnTransaction the entity to save.
     * @return the persisted entity.
     */
    ReturnTransaction save(ReturnTransaction returnTransaction);

    /**
     * Partially updates a returnTransaction.
     *
     * @param returnTransaction the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReturnTransaction> partialUpdate(ReturnTransaction returnTransaction);

    /**
     * Get all the returnTransactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReturnTransaction> findAll(Pageable pageable);

    /**
     * Get the "id" returnTransaction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReturnTransaction> findOne(Long id);

    /**
     * Delete the "id" returnTransaction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

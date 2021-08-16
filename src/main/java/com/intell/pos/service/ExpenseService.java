package com.intell.pos.service;

import com.intell.pos.domain.Expense;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Expense}.
 */
public interface ExpenseService {
    /**
     * Save a expense.
     *
     * @param expense the entity to save.
     * @return the persisted entity.
     */
    Expense save(Expense expense);

    /**
     * Partially updates a expense.
     *
     * @param expense the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Expense> partialUpdate(Expense expense);

    /**
     * Get all the expenses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Expense> findAll(Pageable pageable);

    /**
     * Get the "id" expense.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Expense> findOne(Long id);

    /**
     * Delete the "id" expense.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

package com.intell.pos.service;

import com.intell.pos.domain.ExpenseCategorie;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ExpenseCategorie}.
 */
public interface ExpenseCategorieService {
    /**
     * Save a expenseCategorie.
     *
     * @param expenseCategorie the entity to save.
     * @return the persisted entity.
     */
    ExpenseCategorie save(ExpenseCategorie expenseCategorie);

    /**
     * Partially updates a expenseCategorie.
     *
     * @param expenseCategorie the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExpenseCategorie> partialUpdate(ExpenseCategorie expenseCategorie);

    /**
     * Get all the expenseCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExpenseCategorie> findAll(Pageable pageable);

    /**
     * Get the "id" expenseCategorie.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExpenseCategorie> findOne(Long id);

    /**
     * Delete the "id" expenseCategorie.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

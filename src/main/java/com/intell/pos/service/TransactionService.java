package com.intell.pos.service;

import com.intell.pos.domain.Transaction;
import com.intell.pos.domain.enumeration.TransactionTypes;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Transaction}.
 */
public interface TransactionService {
    /**
     * Save a transaction.
     *
     * @param transaction the entity to save.
     * @return the persisted entity.
     */
    Transaction save(Transaction transaction);

    /**
     * Partially updates a transaction.
     *
     * @param transaction the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Transaction> partialUpdate(Transaction transaction);

    /**
     * Get all the transactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Transaction> findAll(Pageable pageable);

    /**
     * Get the "id" transaction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Transaction> findOne(Long id);

    /**
     * Get the "ref" transaction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Transaction findByRef(String ref);

    /**
     * Delete the "id" transaction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<Transaction> findByTransactiontype(TransactionTypes transactionType);

    Page<Transaction> findByTransactiontype(TransactionTypes transactionType, Pageable pageable);

    Page<Transaction> findByTransactiontypeAndPersonId(TransactionTypes transactionType, Long personId, Pageable pageable);

    /**
     * Get transactions type sell by product ID.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Transaction> findByProductIdTypeSell(Long productId, Pageable pageable);

    /**
     * Get transactions type purchase by product ID.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Transaction> findByProductIdTypePurchase(Long productId, Pageable pageable);

    int countByPersonId(Long personId);

    int countByProductId(Long productId);

    double totalAmountByPersonId(Long personId);
}

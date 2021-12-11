package com.intell.pos.repository;

import com.intell.pos.domain.Transaction;
import com.intell.pos.domain.enumeration.TransactionTypes;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Transaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("select t from Transaction t where t.transactionType = ?1")
    Page<Transaction> findByTransactionType(TransactionTypes transactionType, Pageable pageable);

    @Query("select t from Transaction t where t.transactionType = ?1")
    List<Transaction> findByTransactionType(TransactionTypes transactionType);

    @Query("select t from Transaction t where t.referenceNo = ?1 ")
    public Transaction findFirstByReferenceNo(String reference);

    @Query("select t from Transaction t where " + "?1 in (select s.product.id from Sell s where s.referenceNo = t.referenceNo)")
    public Page<Transaction> findTansactionSellByProductId(Long productId, Pageable pageable);

    @Query("select t from Transaction t where " + "?1 in (select s.product.id from Purchase s where s.referenceNo = t.referenceNo)")
    public Page<Transaction> findTansactionPurchaseByProductId(Long productId, Pageable pageable);
}

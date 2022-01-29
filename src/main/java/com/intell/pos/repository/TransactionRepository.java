package com.intell.pos.repository;

import com.intell.pos.domain.Transaction;
import com.intell.pos.domain.enumeration.TransactionTypes;
import com.intell.pos.domain.projection.ITransactionResume;
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

    @Query("select t from Transaction t where t.transactionType = ?1 and t.person.id = ?2")
    Page<Transaction> findByTransactionTypeAndPersonId(TransactionTypes transactionType, Long personId, Pageable pageable);

    @Query("select t from Transaction t where t.referenceNo = ?1 ")
    public Transaction findFirstByReferenceNo(String reference);

    @Query("select t from Transaction t where ?1 in (select s.product.id from Sell s where s.referenceNo = t.referenceNo)")
    public Page<Transaction> findTansactionSellByProductId(Long productId, Pageable pageable);

    @Query("select t from Transaction t where ?1 in (select s.product.id from Purchase s where s.referenceNo = t.referenceNo)")
    public Page<Transaction> findTansactionPurchaseByProductId(Long productId, Pageable pageable);

    @Query("select count(t) from Transaction t where t.person.id = ?1")
    int countByPersonId(Long personId);

    @Query("select sum(t.netTotal) from Transaction t where t.person.id = ?1")
    double totalAmountByPersonId(Long personId);

    @Query(
        "select count(t) from Transaction t where " +
        "?1 in (select s.product.id from Sell s where s.referenceNo = t.referenceNo) " +
        "or " +
        "?1 in (select s.product.id from Purchase s where s.referenceNo = t.referenceNo)"
    )
    int countByProductId(Long productId);

    @Query("select t from Transaction t where t.transactionType = ?1 and t.createdAt > current_date")
    Page<Transaction> findTodayTransactionsByTransactionType(TransactionTypes transactionType, Pageable pageable);

    @Query(
        "select sum(t.netTotal) as total, sum(t.paid) as paid from Transaction t where t.transactionType = ?1 and t.createdAt > current_date"
    )
    ITransactionResume findTodayResumeByTransactionType(TransactionTypes transactionType);

    @Query("select sum(t.netTotal) as total, sum(t.paid) as paid from Transaction t where t.transactionType = ?1")
    ITransactionResume findResumeByTransactionType(TransactionTypes transactionType);
}

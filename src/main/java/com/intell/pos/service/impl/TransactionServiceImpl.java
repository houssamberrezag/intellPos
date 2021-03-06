package com.intell.pos.service.impl;

import com.intell.pos.domain.Transaction;
import com.intell.pos.domain.enumeration.TransactionTypes;
import com.intell.pos.domain.projection.ITransactionResume;
import com.intell.pos.repository.TransactionRepository;
import com.intell.pos.service.TransactionService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Transaction}.
 */
@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        log.debug("Request to save Transaction : {}", transaction);
        return transactionRepository.save(transaction);
    }

    @Override
    public Optional<Transaction> partialUpdate(Transaction transaction) {
        log.debug("Request to partially update Transaction : {}", transaction);

        return transactionRepository
            .findById(transaction.getId())
            .map(
                existingTransaction -> {
                    if (transaction.getReferenceNo() != null) {
                        existingTransaction.setReferenceNo(transaction.getReferenceNo());
                    }
                    if (transaction.getTransactionType() != null) {
                        existingTransaction.setTransactionType(transaction.getTransactionType());
                    }
                    if (transaction.getTotalCostPrice() != null) {
                        existingTransaction.setTotalCostPrice(transaction.getTotalCostPrice());
                    }
                    if (transaction.getDiscount() != null) {
                        existingTransaction.setDiscount(transaction.getDiscount());
                    }
                    if (transaction.getTotal() != null) {
                        existingTransaction.setTotal(transaction.getTotal());
                    }
                    if (transaction.getInvoiceTax() != null) {
                        existingTransaction.setInvoiceTax(transaction.getInvoiceTax());
                    }
                    if (transaction.getTotalTax() != null) {
                        existingTransaction.setTotalTax(transaction.getTotalTax());
                    }
                    if (transaction.getLaborCost() != null) {
                        existingTransaction.setLaborCost(transaction.getLaborCost());
                    }
                    if (transaction.getNetTotal() != null) {
                        existingTransaction.setNetTotal(transaction.getNetTotal());
                    }
                    if (transaction.getPaid() != null) {
                        existingTransaction.setPaid(transaction.getPaid());
                    }
                    if (transaction.getChangeAmount() != null) {
                        existingTransaction.setChangeAmount(transaction.getChangeAmount());
                    }
                    if (transaction.getReturnInvoice() != null) {
                        existingTransaction.setReturnInvoice(transaction.getReturnInvoice());
                    }
                    if (transaction.getReturnBalance() != null) {
                        existingTransaction.setReturnBalance(transaction.getReturnBalance());
                    }
                    if (transaction.getPos() != null) {
                        existingTransaction.setPos(transaction.getPos());
                    }
                    if (transaction.getDate() != null) {
                        existingTransaction.setDate(transaction.getDate());
                    }
                    if (transaction.getCreatedAt() != null) {
                        existingTransaction.setCreatedAt(transaction.getCreatedAt());
                    }
                    if (transaction.getUpdatedAt() != null) {
                        existingTransaction.setUpdatedAt(transaction.getUpdatedAt());
                    }
                    if (transaction.getDeletedAt() != null) {
                        existingTransaction.setDeletedAt(transaction.getDeletedAt());
                    }

                    return existingTransaction;
                }
            )
            .map(transactionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Transaction> findAll(Pageable pageable) {
        log.debug("Request to get all Transactions");
        return transactionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Transaction> findOne(Long id) {
        log.debug("Request to get Transaction : {}", id);
        return transactionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Transaction : {}", id);
        transactionRepository.deleteById(id);
    }

    @Override
    public List<Transaction> findByTransactiontype(TransactionTypes transactionType) {
        // TODO Auto-generated method stub
        return transactionRepository.findByTransactionType(transactionType);
    }

    @Override
    public Page<Transaction> findByTransactiontype(TransactionTypes transactionType, Pageable pageable) {
        // TODO Auto-generated method stub
        return transactionRepository.findByTransactionType(transactionType, pageable);
    }

    @Override
    public Transaction findByRef(String ref) {
        // TODO Auto-generated method stub
        return transactionRepository.findFirstByReferenceNo(ref);
    }

    @Override
    public Page<Transaction> findByProductIdTypeSell(Long productId, Pageable pageable) {
        // TODO Auto-generated method stub
        return transactionRepository.findTansactionSellByProductId(productId, pageable);
    }

    @Override
    public Page<Transaction> findByProductIdTypePurchase(Long productId, Pageable pageable) {
        // TODO Auto-generated method stub
        return transactionRepository.findTansactionPurchaseByProductId(productId, pageable);
    }

    @Override
    public int countByPersonId(Long personId) {
        return transactionRepository.countByPersonId(personId);
    }

    @Override
    public int countByProductId(Long productId) {
        return transactionRepository.countByProductId(productId);
    }

    @Override
    public Page<Transaction> findByTransactiontypeAndPersonId(TransactionTypes transactionType, Long personId, Pageable pageable) {
        return transactionRepository.findByTransactionTypeAndPersonId(transactionType, personId, pageable);
    }

    @Override
    public double totalAmountByPersonId(Long personId) {
        return transactionRepository.totalAmountByPersonId(personId);
    }

    @Override
    public Page<Transaction> findTodayTransactionsByTransactiontype(TransactionTypes transactionType, Pageable pageable) {
        return transactionRepository.findTodayTransactionsByTransactionType(transactionType, pageable);
    }

    @Override
    public ITransactionResume findResumeByTransactionType(TransactionTypes transactionType) {
        return transactionRepository.findResumeByTransactionType(transactionType);
    }

    @Override
    public ITransactionResume findTodayResumeByTransactionType(TransactionTypes transactionType) {
        return transactionRepository.findTodayResumeByTransactionType(transactionType);
    }
}

package com.intell.pos.service.impl;

import com.intell.pos.domain.ReturnTransaction;
import com.intell.pos.repository.ReturnTransactionRepository;
import com.intell.pos.service.ReturnTransactionService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ReturnTransaction}.
 */
@Service
@Transactional
public class ReturnTransactionServiceImpl implements ReturnTransactionService {

    private final Logger log = LoggerFactory.getLogger(ReturnTransactionServiceImpl.class);

    private final ReturnTransactionRepository returnTransactionRepository;

    public ReturnTransactionServiceImpl(ReturnTransactionRepository returnTransactionRepository) {
        this.returnTransactionRepository = returnTransactionRepository;
    }

    @Override
    public ReturnTransaction save(ReturnTransaction returnTransaction) {
        log.debug("Request to save ReturnTransaction : {}", returnTransaction);
        return returnTransactionRepository.save(returnTransaction);
    }

    @Override
    public Optional<ReturnTransaction> partialUpdate(ReturnTransaction returnTransaction) {
        log.debug("Request to partially update ReturnTransaction : {}", returnTransaction);

        return returnTransactionRepository
            .findById(returnTransaction.getId())
            .map(
                existingReturnTransaction -> {
                    if (returnTransaction.getReturnVat() != null) {
                        existingReturnTransaction.setReturnVat(returnTransaction.getReturnVat());
                    }
                    if (returnTransaction.getSellsReferenceNo() != null) {
                        existingReturnTransaction.setSellsReferenceNo(returnTransaction.getSellsReferenceNo());
                    }
                    if (returnTransaction.getReturnUnits() != null) {
                        existingReturnTransaction.setReturnUnits(returnTransaction.getReturnUnits());
                    }
                    if (returnTransaction.getReturnAmount() != null) {
                        existingReturnTransaction.setReturnAmount(returnTransaction.getReturnAmount());
                    }
                    if (returnTransaction.getReturnedBy() != null) {
                        existingReturnTransaction.setReturnedBy(returnTransaction.getReturnedBy());
                    }
                    if (returnTransaction.getCreatedAt() != null) {
                        existingReturnTransaction.setCreatedAt(returnTransaction.getCreatedAt());
                    }
                    if (returnTransaction.getUpdatedAt() != null) {
                        existingReturnTransaction.setUpdatedAt(returnTransaction.getUpdatedAt());
                    }
                    if (returnTransaction.getDeletedAt() != null) {
                        existingReturnTransaction.setDeletedAt(returnTransaction.getDeletedAt());
                    }

                    return existingReturnTransaction;
                }
            )
            .map(returnTransactionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReturnTransaction> findAll(Pageable pageable) {
        log.debug("Request to get all ReturnTransactions");
        return returnTransactionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReturnTransaction> findOne(Long id) {
        log.debug("Request to get ReturnTransaction : {}", id);
        return returnTransactionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ReturnTransaction : {}", id);
        returnTransactionRepository.deleteById(id);
    }
}

package com.intell.pos.service.impl;

import com.intell.pos.domain.Expense;
import com.intell.pos.repository.ExpenseRepository;
import com.intell.pos.service.ExpenseService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Expense}.
 */
@Service
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

    private final Logger log = LoggerFactory.getLogger(ExpenseServiceImpl.class);

    private final ExpenseRepository expenseRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public Expense save(Expense expense) {
        log.debug("Request to save Expense : {}", expense);
        return expenseRepository.save(expense);
    }

    @Override
    public Optional<Expense> partialUpdate(Expense expense) {
        log.debug("Request to partially update Expense : {}", expense);

        return expenseRepository
            .findById(expense.getId())
            .map(
                existingExpense -> {
                    if (expense.getPurpose() != null) {
                        existingExpense.setPurpose(expense.getPurpose());
                    }
                    if (expense.getAmount() != null) {
                        existingExpense.setAmount(expense.getAmount());
                    }
                    if (expense.getCreatedAt() != null) {
                        existingExpense.setCreatedAt(expense.getCreatedAt());
                    }
                    if (expense.getUpdatedAt() != null) {
                        existingExpense.setUpdatedAt(expense.getUpdatedAt());
                    }
                    if (expense.getDeletedAt() != null) {
                        existingExpense.setDeletedAt(expense.getDeletedAt());
                    }

                    return existingExpense;
                }
            )
            .map(expenseRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Expense> findAll(Pageable pageable) {
        log.debug("Request to get all Expenses");
        return expenseRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Expense> findOne(Long id) {
        log.debug("Request to get Expense : {}", id);
        return expenseRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Expense : {}", id);
        expenseRepository.deleteById(id);
    }
}

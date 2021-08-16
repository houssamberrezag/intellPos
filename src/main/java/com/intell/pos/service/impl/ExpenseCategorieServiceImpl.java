package com.intell.pos.service.impl;

import com.intell.pos.domain.ExpenseCategorie;
import com.intell.pos.repository.ExpenseCategorieRepository;
import com.intell.pos.service.ExpenseCategorieService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ExpenseCategorie}.
 */
@Service
@Transactional
public class ExpenseCategorieServiceImpl implements ExpenseCategorieService {

    private final Logger log = LoggerFactory.getLogger(ExpenseCategorieServiceImpl.class);

    private final ExpenseCategorieRepository expenseCategorieRepository;

    public ExpenseCategorieServiceImpl(ExpenseCategorieRepository expenseCategorieRepository) {
        this.expenseCategorieRepository = expenseCategorieRepository;
    }

    @Override
    public ExpenseCategorie save(ExpenseCategorie expenseCategorie) {
        log.debug("Request to save ExpenseCategorie : {}", expenseCategorie);
        return expenseCategorieRepository.save(expenseCategorie);
    }

    @Override
    public Optional<ExpenseCategorie> partialUpdate(ExpenseCategorie expenseCategorie) {
        log.debug("Request to partially update ExpenseCategorie : {}", expenseCategorie);

        return expenseCategorieRepository
            .findById(expenseCategorie.getId())
            .map(
                existingExpenseCategorie -> {
                    if (expenseCategorie.getName() != null) {
                        existingExpenseCategorie.setName(expenseCategorie.getName());
                    }
                    if (expenseCategorie.getCreatedAt() != null) {
                        existingExpenseCategorie.setCreatedAt(expenseCategorie.getCreatedAt());
                    }
                    if (expenseCategorie.getUpdatedAt() != null) {
                        existingExpenseCategorie.setUpdatedAt(expenseCategorie.getUpdatedAt());
                    }

                    return existingExpenseCategorie;
                }
            )
            .map(expenseCategorieRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExpenseCategorie> findAll(Pageable pageable) {
        log.debug("Request to get all ExpenseCategories");
        return expenseCategorieRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExpenseCategorie> findOne(Long id) {
        log.debug("Request to get ExpenseCategorie : {}", id);
        return expenseCategorieRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExpenseCategorie : {}", id);
        expenseCategorieRepository.deleteById(id);
    }
}

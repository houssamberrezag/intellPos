package com.intell.pos.repository;

import com.intell.pos.domain.ExpenseCategorie;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ExpenseCategorie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExpenseCategorieRepository extends JpaRepository<ExpenseCategorie, Long> {}

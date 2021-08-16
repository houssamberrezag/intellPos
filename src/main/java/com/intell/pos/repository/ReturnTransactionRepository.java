package com.intell.pos.repository;

import com.intell.pos.domain.ReturnTransaction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ReturnTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReturnTransactionRepository extends JpaRepository<ReturnTransaction, Long> {}

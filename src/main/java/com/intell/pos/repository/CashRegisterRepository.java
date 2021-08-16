package com.intell.pos.repository;

import com.intell.pos.domain.CashRegister;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CashRegister entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CashRegisterRepository extends JpaRepository<CashRegister, Long> {}

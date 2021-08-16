package com.intell.pos.repository;

import com.intell.pos.domain.Taxe;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Taxe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaxeRepository extends JpaRepository<Taxe, Long> {}

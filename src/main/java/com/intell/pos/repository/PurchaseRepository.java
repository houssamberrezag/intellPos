package com.intell.pos.repository;

import com.intell.pos.domain.Purchase;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Purchase entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    @Query("select p from Purchase p where p.referenceNo = ?1")
    List<Purchase> findByReference(String reference);

    Page<Purchase> findByProductId(Long productId, Pageable pageable);
}

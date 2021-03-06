package com.intell.pos.repository;

import com.intell.pos.domain.Purchase;
import com.intell.pos.domain.projection.ITodaySellsAndItems;
import java.time.Instant;
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

    @Query("select sum(p.quantity) from Purchase p where p.person.id = ?1")
    int findtotalQuantityByPersonId(Long personId);

    @Query(
        value = "select sum(p.quantity) as quantite, sum(p.subTotal) as total " +
        "from Purchase p " +
        "where p.createdAt is not null " +
        "and " +
        "p.createdAt >= ?1 " +
        "and " +
        "p.createdAt <= ?2 "
    )
    ITodaySellsAndItems findSumQuantitesAndItems(Instant debut, Instant fin);
}

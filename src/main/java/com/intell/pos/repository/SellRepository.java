package com.intell.pos.repository;

import com.intell.pos.domain.Purchase;
import com.intell.pos.domain.Sell;
import com.intell.pos.domain.projection.ITodaySellsAndItems;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Sell entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SellRepository extends JpaRepository<Sell, Long> {
    @Query("select s from Sell s where s.referenceNo = ?1")
    List<Sell> findByReference(String reference);

    Page<Sell> findByProductId(Long productId, Pageable pageable);

    @Query("select sum(s.quantity) from Sell s where s.person.id = ?1")
    int findtotalQuantityByPersonId(Long personId);

    @Query(
        value = "select sum(s.quantity) as quantite, sum(s.subTotal) as total " +
        "from Sell s " +
        "where s.createdAt is not null " +
        "and " +
        "s.createdAt >= ?1 " +
        "and " +
        "s.createdAt <= ?2 "
    )
    ITodaySellsAndItems findSumQuantitesAndItems(Instant debut, Instant fin);
}

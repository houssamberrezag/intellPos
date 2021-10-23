package com.intell.pos.repository;

import com.intell.pos.domain.Purchase;
import com.intell.pos.domain.Sell;
import java.util.List;
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
}

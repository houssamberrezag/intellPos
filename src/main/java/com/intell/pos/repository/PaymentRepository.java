package com.intell.pos.repository;

import com.intell.pos.domain.Payment;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Payment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("select p from Payment p where p.referenceNo = ?1 order by id desc")
    List<Payment> findByReference(String reference);

    Page<Payment> findByPersonId(Long personId, Pageable pageable);

    @Query("select sum(p.amount) from Payment p where p.person.id = ?1")
    double totalAmountByPersonId(Long personId);

    @Query(
        "select sum(p.amount) from Payment p where " +
        "p.createdAt is not null " +
        "and " +
        "p.createdAt >= ?1 " +
        "and " +
        "p.createdAt <= ?2 "
    )
    double totalAmountBitweenToDates(Instant debut, Instant fin);

    @Query("select p from Payment p where p.createdAt > current_date")
    Page<Payment> todayPayments(Pageable pageable);
}

package com.intell.pos.service;

import com.intell.pos.domain.Payment;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Payment}.
 */
public interface PaymentService {
    /**
     * Save a payment.
     *
     * @param payment the entity to save.
     * @return the persisted entity.
     */
    Payment save(Payment payment);

    /**
     * Save a payment.
     *
     * @param payment the entity to save.
     * @return the persisted entity.
     */
    Payment saveAndUpdateTransactionPaidAmount(Payment payment);

    /**
     * Partially updates a payment.
     *
     * @param payment the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Payment> partialUpdate(Payment payment);

    /**
     * Get all the payments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Payment> findAll(Pageable pageable);

    /**
     * Get payments by person id.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Payment> findByPersonId(Long personId, Pageable pageable);

    /**
     * Get the "id" payment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Payment> findOne(Long id);

    /**
     * Delete the "id" payment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get payments by reference.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    List<Payment> findByReference(String reference);

    double totalAmountByPersonId(Long personId);

    double totalAmountBitweenToDates(Instant debut, Instant fin);

    /**
     * Get today payments .
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Payment> todayPayments(Pageable pageable);
}

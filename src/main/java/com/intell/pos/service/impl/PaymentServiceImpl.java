package com.intell.pos.service.impl;

import com.intell.pos.domain.Payment;
import com.intell.pos.domain.Transaction;
import com.intell.pos.repository.PaymentRepository;
import com.intell.pos.repository.TransactionRepository;
import com.intell.pos.service.PaymentService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Payment}.
 */
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;

    private final TransactionRepository transactionRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, TransactionRepository transactionRepository) {
        this.paymentRepository = paymentRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Payment save(Payment payment) {
        log.debug("Request to save Payment : {}", payment);
        return paymentRepository.save(payment);
    }

    @Override
    public Optional<Payment> partialUpdate(Payment payment) {
        log.debug("Request to partially update Payment : {}", payment);

        return paymentRepository
            .findById(payment.getId())
            .map(
                existingPayment -> {
                    if (payment.getAmount() != null) {
                        existingPayment.setAmount(payment.getAmount());
                    }
                    if (payment.getMethod() != null) {
                        existingPayment.setMethod(payment.getMethod());
                    }
                    if (payment.getType() != null) {
                        existingPayment.setType(payment.getType());
                    }
                    if (payment.getReferenceNo() != null) {
                        existingPayment.setReferenceNo(payment.getReferenceNo());
                    }
                    if (payment.getNote() != null) {
                        existingPayment.setNote(payment.getNote());
                    }
                    if (payment.getDate() != null) {
                        existingPayment.setDate(payment.getDate());
                    }
                    if (payment.getCreatedAt() != null) {
                        existingPayment.setCreatedAt(payment.getCreatedAt());
                    }
                    if (payment.getUpdatedAt() != null) {
                        existingPayment.setUpdatedAt(payment.getUpdatedAt());
                    }
                    if (payment.getDeletedAt() != null) {
                        existingPayment.setDeletedAt(payment.getDeletedAt());
                    }

                    return existingPayment;
                }
            )
            .map(paymentRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Payment> findAll(Pageable pageable) {
        log.debug("Request to get all Payments");
        return paymentRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Payment> findOne(Long id) {
        log.debug("Request to get Payment : {}", id);
        return paymentRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Payment : {}", id);
        paymentRepository.deleteById(id);
    }

    @Override
    public List<Payment> findByReference(String reference) {
        log.debug("Request to get Payments by reference");
        return paymentRepository.findByReference(reference);
    }

    @Override
    public Payment saveAndUpdateTransactionPaidAmount(Payment payment) {
        log.debug("Request to save Payment : {}, and update transaction paid amount ", payment);
        Transaction transaction = transactionRepository.findFirstByReferenceNo(payment.getReferenceNo());
        if (transaction != null) {
            transaction.setPaid(transaction.getPaid() + payment.getAmount());
            transactionRepository.save(transaction);
        }
        return paymentRepository.save(payment);
    }

    @Override
    public Page<Payment> findByPersonId(Long personId, Pageable pageable) {
        log.debug("Request to get page of Payments by person id");
        return paymentRepository.findByPersonId(personId, pageable);
    }

    @Override
    public double totalAmountByPersonId(Long personId) {
        // TODO Auto-generated method stub
        return paymentRepository.totalAmountByPersonId(personId);
    }
}

package com.intell.pos.service.impl;

import com.intell.pos.domain.Purchase;
import com.intell.pos.repository.PurchaseRepository;
import com.intell.pos.service.PurchaseService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Purchase}.
 */
@Service
@Transactional
public class PurchaseServiceImpl implements PurchaseService {

    private final Logger log = LoggerFactory.getLogger(PurchaseServiceImpl.class);

    private final PurchaseRepository purchaseRepository;

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public Purchase save(Purchase purchase) {
        log.debug("Request to save Purchase : {}", purchase);
        return purchaseRepository.save(purchase);
    }

    @Override
    public Optional<Purchase> partialUpdate(Purchase purchase) {
        log.debug("Request to partially update Purchase : {}", purchase);

        return purchaseRepository
            .findById(purchase.getId())
            .map(
                existingPurchase -> {
                    if (purchase.getReferenceNo() != null) {
                        existingPurchase.setReferenceNo(purchase.getReferenceNo());
                    }
                    if (purchase.getQuantity() != null) {
                        existingPurchase.setQuantity(purchase.getQuantity());
                    }
                    if (purchase.getSubTotal() != null) {
                        existingPurchase.setSubTotal(purchase.getSubTotal());
                    }
                    if (purchase.getProductTax() != null) {
                        existingPurchase.setProductTax(purchase.getProductTax());
                    }
                    if (purchase.getDate() != null) {
                        existingPurchase.setDate(purchase.getDate());
                    }
                    if (purchase.getCreatedAt() != null) {
                        existingPurchase.setCreatedAt(purchase.getCreatedAt());
                    }
                    if (purchase.getUpdatedAt() != null) {
                        existingPurchase.setUpdatedAt(purchase.getUpdatedAt());
                    }
                    if (purchase.getDeletedAt() != null) {
                        existingPurchase.setDeletedAt(purchase.getDeletedAt());
                    }

                    return existingPurchase;
                }
            )
            .map(purchaseRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Purchase> findAll(Pageable pageable) {
        log.debug("Request to get all Purchases");
        return purchaseRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Purchase> findOne(Long id) {
        log.debug("Request to get Purchase : {}", id);
        return purchaseRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Purchase : {}", id);
        purchaseRepository.deleteById(id);
    }

    @Override
    public List<Purchase> findByReference(String reference) {
        log.debug("Request to get Purchases By reference");
        return purchaseRepository.findByReference(reference);
    }

    @Override
    public Page<Purchase> findByProductId(Long productId, Pageable pageable) {
        log.debug("Request to get page of Purchases By product id");
        return purchaseRepository.findByProductId(productId, pageable);
    }

    @Override
    public int findtotalQuantityByPersonId(Long personId) {
        return purchaseRepository.findtotalQuantityByPersonId(personId);
    }
}

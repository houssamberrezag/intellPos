package com.intell.pos.service.impl;

import com.intell.pos.domain.Sell;
import com.intell.pos.domain.projection.ITodaySellsAndItems;
import com.intell.pos.repository.SellRepository;
import com.intell.pos.service.SellService;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Sell}.
 */
@Service
@Transactional
public class SellServiceImpl implements SellService {

    private final Logger log = LoggerFactory.getLogger(SellServiceImpl.class);

    private final SellRepository sellRepository;

    public SellServiceImpl(SellRepository sellRepository) {
        this.sellRepository = sellRepository;
    }

    @Override
    public Sell save(Sell sell) {
        log.debug("Request to save Sell : {}", sell);
        return sellRepository.save(sell);
    }

    @Override
    public Optional<Sell> partialUpdate(Sell sell) {
        log.debug("Request to partially update Sell : {}", sell);

        return sellRepository
            .findById(sell.getId())
            .map(
                existingSell -> {
                    if (sell.getReferenceNo() != null) {
                        existingSell.setReferenceNo(sell.getReferenceNo());
                    }
                    if (sell.getQuantity() != null) {
                        existingSell.setQuantity(sell.getQuantity());
                    }
                    if (sell.getUnitCostPrice() != null) {
                        existingSell.setUnitCostPrice(sell.getUnitCostPrice());
                    }
                    if (sell.getSubTotal() != null) {
                        existingSell.setSubTotal(sell.getSubTotal());
                    }
                    if (sell.getProductTax() != null) {
                        existingSell.setProductTax(sell.getProductTax());
                    }
                    if (sell.getDate() != null) {
                        existingSell.setDate(sell.getDate());
                    }
                    if (sell.getCreatedAt() != null) {
                        existingSell.setCreatedAt(sell.getCreatedAt());
                    }
                    if (sell.getUpdatedAt() != null) {
                        existingSell.setUpdatedAt(sell.getUpdatedAt());
                    }
                    if (sell.getDeletedAt() != null) {
                        existingSell.setDeletedAt(sell.getDeletedAt());
                    }

                    return existingSell;
                }
            )
            .map(sellRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Sell> findAll(Pageable pageable) {
        log.debug("Request to get all Sells");
        return sellRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Sell> findOne(Long id) {
        log.debug("Request to get Sell : {}", id);
        return sellRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sell : {}", id);
        sellRepository.deleteById(id);
    }

    @Override
    public List<Sell> findByReference(String reference) {
        return sellRepository.findByReference(reference);
    }

    @Override
    public Page<Sell> findByProductId(Long productId, Pageable pageable) {
        log.debug("Request to get page of sells By product id");
        return sellRepository.findByProductId(productId, pageable);
    }

    @Override
    public int findtotalQuantityByPersonId(Long personId) {
        return sellRepository.findtotalQuantityByPersonId(personId);
    }

    @Override
    public ITodaySellsAndItems findSumQuantitesAndItems(Instant debut, Instant fin) {
        return sellRepository.findSumQuantitesAndItems(debut, fin);
    }
}

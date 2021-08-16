package com.intell.pos.service.impl;

import com.intell.pos.domain.Taxe;
import com.intell.pos.repository.TaxeRepository;
import com.intell.pos.service.TaxeService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Taxe}.
 */
@Service
@Transactional
public class TaxeServiceImpl implements TaxeService {

    private final Logger log = LoggerFactory.getLogger(TaxeServiceImpl.class);

    private final TaxeRepository taxeRepository;

    public TaxeServiceImpl(TaxeRepository taxeRepository) {
        this.taxeRepository = taxeRepository;
    }

    @Override
    public Taxe save(Taxe taxe) {
        log.debug("Request to save Taxe : {}", taxe);
        return taxeRepository.save(taxe);
    }

    @Override
    public Optional<Taxe> partialUpdate(Taxe taxe) {
        log.debug("Request to partially update Taxe : {}", taxe);

        return taxeRepository
            .findById(taxe.getId())
            .map(
                existingTaxe -> {
                    if (taxe.getName() != null) {
                        existingTaxe.setName(taxe.getName());
                    }
                    if (taxe.getRate() != null) {
                        existingTaxe.setRate(taxe.getRate());
                    }
                    if (taxe.getType() != null) {
                        existingTaxe.setType(taxe.getType());
                    }
                    if (taxe.getCreatedAt() != null) {
                        existingTaxe.setCreatedAt(taxe.getCreatedAt());
                    }
                    if (taxe.getUpdatedAt() != null) {
                        existingTaxe.setUpdatedAt(taxe.getUpdatedAt());
                    }

                    return existingTaxe;
                }
            )
            .map(taxeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Taxe> findAll(Pageable pageable) {
        log.debug("Request to get all Taxes");
        return taxeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Taxe> findOne(Long id) {
        log.debug("Request to get Taxe : {}", id);
        return taxeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Taxe : {}", id);
        taxeRepository.deleteById(id);
    }
}

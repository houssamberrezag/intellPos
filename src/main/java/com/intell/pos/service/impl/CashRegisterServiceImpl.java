package com.intell.pos.service.impl;

import com.intell.pos.domain.CashRegister;
import com.intell.pos.repository.CashRegisterRepository;
import com.intell.pos.service.CashRegisterService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CashRegister}.
 */
@Service
@Transactional
public class CashRegisterServiceImpl implements CashRegisterService {

    private final Logger log = LoggerFactory.getLogger(CashRegisterServiceImpl.class);

    private final CashRegisterRepository cashRegisterRepository;

    public CashRegisterServiceImpl(CashRegisterRepository cashRegisterRepository) {
        this.cashRegisterRepository = cashRegisterRepository;
    }

    @Override
    public CashRegister save(CashRegister cashRegister) {
        log.debug("Request to save CashRegister : {}", cashRegister);
        return cashRegisterRepository.save(cashRegister);
    }

    @Override
    public Optional<CashRegister> partialUpdate(CashRegister cashRegister) {
        log.debug("Request to partially update CashRegister : {}", cashRegister);

        return cashRegisterRepository
            .findById(cashRegister.getId())
            .map(
                existingCashRegister -> {
                    if (cashRegister.getCashInHands() != null) {
                        existingCashRegister.setCashInHands(cashRegister.getCashInHands());
                    }
                    if (cashRegister.getDate() != null) {
                        existingCashRegister.setDate(cashRegister.getDate());
                    }
                    if (cashRegister.getCreatedAt() != null) {
                        existingCashRegister.setCreatedAt(cashRegister.getCreatedAt());
                    }
                    if (cashRegister.getUpdatedAt() != null) {
                        existingCashRegister.setUpdatedAt(cashRegister.getUpdatedAt());
                    }

                    return existingCashRegister;
                }
            )
            .map(cashRegisterRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CashRegister> findAll(Pageable pageable) {
        log.debug("Request to get all CashRegisters");
        return cashRegisterRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CashRegister> findOne(Long id) {
        log.debug("Request to get CashRegister : {}", id);
        return cashRegisterRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CashRegister : {}", id);
        cashRegisterRepository.deleteById(id);
    }
}

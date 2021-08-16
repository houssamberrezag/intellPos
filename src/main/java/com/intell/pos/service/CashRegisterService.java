package com.intell.pos.service;

import com.intell.pos.domain.CashRegister;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link CashRegister}.
 */
public interface CashRegisterService {
    /**
     * Save a cashRegister.
     *
     * @param cashRegister the entity to save.
     * @return the persisted entity.
     */
    CashRegister save(CashRegister cashRegister);

    /**
     * Partially updates a cashRegister.
     *
     * @param cashRegister the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CashRegister> partialUpdate(CashRegister cashRegister);

    /**
     * Get all the cashRegisters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CashRegister> findAll(Pageable pageable);

    /**
     * Get the "id" cashRegister.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CashRegister> findOne(Long id);

    /**
     * Delete the "id" cashRegister.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

package com.intell.pos.service.impl;

import com.intell.pos.domain.Damage;
import com.intell.pos.repository.DamageRepository;
import com.intell.pos.service.DamageService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Damage}.
 */
@Service
@Transactional
public class DamageServiceImpl implements DamageService {

    private final Logger log = LoggerFactory.getLogger(DamageServiceImpl.class);

    private final DamageRepository damageRepository;

    public DamageServiceImpl(DamageRepository damageRepository) {
        this.damageRepository = damageRepository;
    }

    @Override
    public Damage save(Damage damage) {
        log.debug("Request to save Damage : {}", damage);
        return damageRepository.save(damage);
    }

    @Override
    public Optional<Damage> partialUpdate(Damage damage) {
        log.debug("Request to partially update Damage : {}", damage);

        return damageRepository
            .findById(damage.getId())
            .map(
                existingDamage -> {
                    if (damage.getQuantity() != null) {
                        existingDamage.setQuantity(damage.getQuantity());
                    }
                    if (damage.getDate() != null) {
                        existingDamage.setDate(damage.getDate());
                    }
                    if (damage.getNote() != null) {
                        existingDamage.setNote(damage.getNote());
                    }
                    if (damage.getCreatedAt() != null) {
                        existingDamage.setCreatedAt(damage.getCreatedAt());
                    }
                    if (damage.getUpdatedAt() != null) {
                        existingDamage.setUpdatedAt(damage.getUpdatedAt());
                    }

                    return existingDamage;
                }
            )
            .map(damageRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Damage> findAll(Pageable pageable) {
        log.debug("Request to get all Damages");
        return damageRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Damage> findOne(Long id) {
        log.debug("Request to get Damage : {}", id);
        return damageRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Damage : {}", id);
        damageRepository.deleteById(id);
    }
}

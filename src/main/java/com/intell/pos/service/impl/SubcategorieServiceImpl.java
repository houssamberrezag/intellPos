package com.intell.pos.service.impl;

import com.intell.pos.domain.Subcategorie;
import com.intell.pos.repository.SubcategorieRepository;
import com.intell.pos.service.SubcategorieService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Subcategorie}.
 */
@Service
@Transactional
public class SubcategorieServiceImpl implements SubcategorieService {

    private final Logger log = LoggerFactory.getLogger(SubcategorieServiceImpl.class);

    private final SubcategorieRepository subcategorieRepository;

    public SubcategorieServiceImpl(SubcategorieRepository subcategorieRepository) {
        this.subcategorieRepository = subcategorieRepository;
    }

    @Override
    public Subcategorie save(Subcategorie subcategorie) {
        log.debug("Request to save Subcategorie : {}", subcategorie);
        return subcategorieRepository.save(subcategorie);
    }

    @Override
    public Optional<Subcategorie> partialUpdate(Subcategorie subcategorie) {
        log.debug("Request to partially update Subcategorie : {}", subcategorie);

        return subcategorieRepository
            .findById(subcategorie.getId())
            .map(
                existingSubcategorie -> {
                    if (subcategorie.getName() != null) {
                        existingSubcategorie.setName(subcategorie.getName());
                    }
                    if (subcategorie.getCreatedAt() != null) {
                        existingSubcategorie.setCreatedAt(subcategorie.getCreatedAt());
                    }
                    if (subcategorie.getUpdatedAt() != null) {
                        existingSubcategorie.setUpdatedAt(subcategorie.getUpdatedAt());
                    }
                    if (subcategorie.getDeletedAt() != null) {
                        existingSubcategorie.setDeletedAt(subcategorie.getDeletedAt());
                    }

                    return existingSubcategorie;
                }
            )
            .map(subcategorieRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Subcategorie> findAll(Pageable pageable) {
        log.debug("Request to get all Subcategories");
        return subcategorieRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Subcategorie> findOne(Long id) {
        log.debug("Request to get Subcategorie : {}", id);
        return subcategorieRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Subcategorie : {}", id);
        subcategorieRepository.deleteById(id);
    }
}

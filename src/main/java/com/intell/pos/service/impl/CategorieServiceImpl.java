package com.intell.pos.service.impl;

import com.intell.pos.domain.Categorie;
import com.intell.pos.repository.CategorieRepository;
import com.intell.pos.service.CategorieService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Categorie}.
 */
@Service
@Transactional
public class CategorieServiceImpl implements CategorieService {

    private final Logger log = LoggerFactory.getLogger(CategorieServiceImpl.class);

    private final CategorieRepository categorieRepository;

    public CategorieServiceImpl(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    @Override
    public Categorie save(Categorie categorie) {
        log.debug("Request to save Categorie : {}", categorie);
        return categorieRepository.save(categorie);
    }

    @Override
    public Optional<Categorie> partialUpdate(Categorie categorie) {
        log.debug("Request to partially update Categorie : {}", categorie);

        return categorieRepository
            .findById(categorie.getId())
            .map(
                existingCategorie -> {
                    if (categorie.getCategoryName() != null) {
                        existingCategorie.setCategoryName(categorie.getCategoryName());
                    }
                    if (categorie.getCreatedAt() != null) {
                        existingCategorie.setCreatedAt(categorie.getCreatedAt());
                    }
                    if (categorie.getUpdatedAt() != null) {
                        existingCategorie.setUpdatedAt(categorie.getUpdatedAt());
                    }
                    if (categorie.getDeletedAt() != null) {
                        existingCategorie.setDeletedAt(categorie.getDeletedAt());
                    }

                    return existingCategorie;
                }
            )
            .map(categorieRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Categorie> findAll(Pageable pageable) {
        log.debug("Request to get all Categories");
        return categorieRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Categorie> findOne(Long id) {
        log.debug("Request to get Categorie : {}", id);
        return categorieRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Categorie : {}", id);
        categorieRepository.deleteById(id);
    }
}

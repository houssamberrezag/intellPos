package com.intell.pos.repository;

import com.intell.pos.domain.Categorie;
import java.util.List;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Categorie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {
    @Override
    @Query("select c from Categorie c where c.deletedAt is null")
    public List<Categorie> findAll();

    @Override
    @Query("select c from Categorie c where c.deletedAt is null")
    public Page<Categorie> findAll(Pageable pageable);
}

package com.intell.pos.repository;

import com.intell.pos.domain.Categorie;
import com.intell.pos.domain.Subcategorie;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Subcategorie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubcategorieRepository extends JpaRepository<Subcategorie, Long> {
    @Override
    @Query("select c from Subcategorie c where c.deletedAt is null")
    public List<Subcategorie> findAll();

    @Override
    @Query("select c from Subcategorie c where c.deletedAt is null")
    public Page<Subcategorie> findAll(Pageable pageable);
}

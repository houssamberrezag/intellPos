package com.intell.pos.repository;

import com.intell.pos.domain.Subcategorie;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Subcategorie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubcategorieRepository extends JpaRepository<Subcategorie, Long> {}

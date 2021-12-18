package com.intell.pos.repository;

import com.intell.pos.domain.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Product entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p where p.subCategorie.id = ?1")
    Page<Product> findBySubCategorieId(Long subcategoryId, Pageable pageable);

    @Query("select p from Product p where p.categorie.id = ?1")
    Page<Product> findByCategorieId(Long categoryId, Pageable pageable);

    Optional<Product> findByCode(String code);

    @Override
    @Query("select p from Product p where p.deletedAt is null")
    List<Product> findAll();

    @Override
    @Query("select p from Product p where p.deletedAt is null")
    Page<Product> findAll(Pageable pageable);

    @Query(value = "select p.name from " + "product p, " + "(select s from sell s where ) p ", nativeQuery = true)
    List<?> test();
}

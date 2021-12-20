package com.intell.pos.service;

import com.intell.pos.domain.Product;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Product}.
 */
public interface ProductService {
    /**
     * Save a product.
     *
     * @param product the entity to save.
     * @return the persisted entity.
     */
    Product save(Product product);

    /**
     * Partially updates a product.
     *
     * @param product the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Product> partialUpdate(Product product);

    /**
     * Get all the products.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Product> findAll(Pageable pageable);

    /**
     * Get the "id" product.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Product> findOne(Long id);

    /**
     * Get product by code.
     *
     * @param code  of the entity.
     * @return the entity.
     */
    Optional<Product> findByCode(String code);

    /**
     * Delete the "id" product.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<Product> findBySubcategoryId(Long subcategoryId, Pageable pageable);

    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    Page<Product> findProductsInAlertQuantity(Pageable pageable);
}

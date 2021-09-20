package com.intell.pos.service.impl;

import com.intell.pos.domain.Product;
import com.intell.pos.repository.ProductRepository;
import com.intell.pos.service.ProductService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Product}.
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product save(Product product) {
        log.debug("Request to save Product : {}", product);
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> partialUpdate(Product product) {
        log.debug("Request to partially update Product : {}", product);

        return productRepository
            .findById(product.getId())
            .map(
                existingProduct -> {
                    if (product.getName() != null) {
                        existingProduct.setName(product.getName());
                    }
                    if (product.getCode() != null) {
                        existingProduct.setCode(product.getCode());
                    }
                    if (product.getQuantity() != null) {
                        existingProduct.setQuantity(product.getQuantity());
                    }
                    if (product.getDetails() != null) {
                        existingProduct.setDetails(product.getDetails());
                    }
                    if (product.getCostPrice() != null) {
                        existingProduct.setCostPrice(product.getCostPrice());
                    }
                    if (product.getMinimumRetailPrice() != null) {
                        existingProduct.setMinimumRetailPrice(product.getMinimumRetailPrice());
                    }
                    if (product.getUnit() != null) {
                        existingProduct.setUnit(product.getUnit());
                    }
                    if (product.getStatus() != null) {
                        existingProduct.setStatus(product.getStatus());
                    }
                    if (product.getImage() != null) {
                        existingProduct.setImage(product.getImage());
                    }
                    if (product.getOpeningStock() != null) {
                        existingProduct.setOpeningStock(product.getOpeningStock());
                    }
                    if (product.getAlertQuantity() != null) {
                        existingProduct.setAlertQuantity(product.getAlertQuantity());
                    }
                    if (product.getCreatedAt() != null) {
                        existingProduct.setCreatedAt(product.getCreatedAt());
                    }
                    if (product.getUpdatedAt() != null) {
                        existingProduct.setUpdatedAt(product.getUpdatedAt());
                    }
                    if (product.getDeletedAt() != null) {
                        existingProduct.setDeletedAt(product.getDeletedAt());
                    }

                    return existingProduct;
                }
            )
            .map(productRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> findAll(Pageable pageable) {
        log.debug("Request to get all Products");
        return productRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findOne(Long id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Product : {}", id);
        productRepository.deleteById(id);
    }

    @Override
    public Page<Product> findBySubcategoryId(Long subcategoryId, Pageable pageable) {
        // TODO Auto-generated method stub
        return productRepository.findBySubCategorieId(subcategoryId, pageable);
    }
}

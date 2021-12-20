package com.intell.pos.repository;

import com.intell.pos.domain.Product;
import com.intell.pos.domain.projection.IProductReportProjection;
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

    @Query("select p from Product p where p.deletedAt is null and p.quantity <= alertQuantity")
    Page<Product> findProductsInAlertQuantity(Pageable pageable);

    @Query(
        value = " select Id, Name, unite, stock, max(aa.sellQuantity) sellQuantity, max(aa.purchaseQuantity) purchaseQuantity, max(aa.sellTotal) SellTotal,max(aa.purchasesTotal) PurchasesTotal from " +
        "( " +
        "(select p.id id,p.name as name,p.unit unite, p.quantity as stock, sum(s.quantity) sellQuantity,0 purchaseQuantity, sum(s.sub_total) sellTotal,0 purchasesTotal from " +
        "    			product p, sell s " +
        "    		where s.product_id = p.id " +
        "    		group by p.id) " +
        "UNION            " +
        "            " +
        "(select p.id id,p.name as name,p.unit unite, p.quantity as stock,0 sellQuantity,sum(pr.quantity) purchaseQuantity,0 sellTotal, sum(pr.sub_total) purchasesTotal from " +
        "    			product p, purchase pr" +
        "    		where pr.product_id = p.id" +
        "    		group by p.id)" +
        "  ) aa" +
        "  GROUP by aa.id",
        nativeQuery = true
    )
    List<IProductReportProjection> productReport();
}

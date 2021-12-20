package com.intell.pos.domain.projection;

public interface IProductReportProjection {
    Long getId();
    String getName();
    String getUnite();
    Integer getPurchaseQuantity();
    Integer getSellQuantity();
    Double getStock();
    Double getPurchasesTotal();
    Double getSellTotal();
}

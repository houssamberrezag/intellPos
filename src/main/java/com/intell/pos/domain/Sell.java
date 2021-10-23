package com.intell.pos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Sell.
 */
@Entity
@Table(name = "sell")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Sell implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 191)
    @Column(name = "reference_no", length = 191, nullable = false)
    private String referenceNo;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Double quantity;

    @Column(name = "unit_cost_price")
    private Double unitCostPrice;

    @Column(name = "unit_price")
    private Double unitPrice;

    @NotNull
    @Column(name = "sub_total", nullable = false)
    private Double subTotal;

    @Column(name = "product_tax")
    private Double productTax;

    @Column(name = "date")
    private Instant date;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @ManyToOne
    @JsonIgnoreProperties(value = { "payments" }, allowSetters = true)
    private Person person;

    @ManyToOne
    @JsonIgnoreProperties(value = { "categorie", "subCategorie", "taxe" }, allowSetters = true)
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sell id(Long id) {
        this.id = id;
        return this;
    }

    public String getReferenceNo() {
        return this.referenceNo;
    }

    public Sell referenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
        return this;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public Double getQuantity() {
        return this.quantity;
    }

    public Sell quantity(Double quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getUnitCostPrice() {
        return this.unitCostPrice;
    }

    public Sell unitCostPrice(Double unitCostPrice) {
        this.unitCostPrice = unitCostPrice;
        return this;
    }

    public void setUnitCostPrice(Double unitCostPrice) {
        this.unitCostPrice = unitCostPrice;
    }

    public Double getSubTotal() {
        return this.subTotal;
    }

    public Sell subTotal(Double subTotal) {
        this.subTotal = subTotal;
        return this;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Double getProductTax() {
        return this.productTax;
    }

    public Sell productTax(Double productTax) {
        this.productTax = productTax;
        return this;
    }

    public void setProductTax(Double productTax) {
        this.productTax = productTax;
    }

    public Instant getDate() {
        return this.date;
    }

    public Sell date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Sell createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Sell updatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return this.deletedAt;
    }

    public Sell deletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Person getPerson() {
        return this.person;
    }

    public Sell person(Person person) {
        this.setPerson(person);
        return this;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Product getProduct() {
        return this.product;
    }

    public Sell product(Product product) {
        this.setProduct(product);
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sell)) {
            return false;
        }
        return id != null && id.equals(((Sell) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sell{" +
            "id=" + getId() +
            ", referenceNo='" + getReferenceNo() + "'" +
            ", quantity=" + getQuantity() +
            ", unitCostPrice=" + getUnitCostPrice() +
            ", unitPrice=" + getUnitPrice() +
            ", subTotal=" + getSubTotal() +
            ", productTax=" + getProductTax() +
            ", date='" + getDate() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            "}";
    }
}

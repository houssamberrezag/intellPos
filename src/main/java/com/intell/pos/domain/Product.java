package com.intell.pos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 191)
    @Column(name = "name", length = 191, nullable = false)
    private String name;

    @NotNull
    @Size(max = 191)
    @Column(name = "code", length = 191, nullable = false)
    private String code;

    @Column(name = "quantity")
    private Double quantity;

    @Lob
    @Column(name = "details")
    private String details;

    @NotNull
    @Column(name = "cost_price", nullable = false)
    private Double costPrice;

    @Column(name = "minimum_retail_price")
    private Double minimumRetailPrice;

    @Size(max = 11)
    @Column(name = "unit", length = 11)
    private String unit;

    @Column(name = "status")
    private Boolean status;

    @Size(max = 255)
    @Column(name = "image", length = 255)
    private String image;

    @Column(name = "opening_stock")
    private Double openingStock;

    @Column(name = "alert_quantity")
    private Integer alertQuantity;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "subcategories" }, allowSetters = true)
    private Categorie categorie;

    @ManyToOne
    @JsonIgnoreProperties(value = { "categorie" }, allowSetters = true)
    private Subcategorie subCategorie;

    @ManyToOne
    private Taxe taxe;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public Product code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getQuantity() {
        return this.quantity;
    }

    public Product quantity(Double quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getDetails() {
        return this.details;
    }

    public Product details(String details) {
        this.details = details;
        return this;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Double getCostPrice() {
        return this.costPrice;
    }

    public Product costPrice(Double costPrice) {
        this.costPrice = costPrice;
        return this;
    }

    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
    }

    public Double getMinimumRetailPrice() {
        return this.minimumRetailPrice;
    }

    public Product minimumRetailPrice(Double minimumRetailPrice) {
        this.minimumRetailPrice = minimumRetailPrice;
        return this;
    }

    public void setMinimumRetailPrice(Double minimumRetailPrice) {
        this.minimumRetailPrice = minimumRetailPrice;
    }

    public String getUnit() {
        return this.unit;
    }

    public Product unit(String unit) {
        this.unit = unit;
        return this;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public Product status(Boolean status) {
        this.status = status;
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getImage() {
        return this.image;
    }

    public Product image(String image) {
        this.image = image;
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getOpeningStock() {
        return this.openingStock;
    }

    public Product openingStock(Double openingStock) {
        this.openingStock = openingStock;
        return this;
    }

    public void setOpeningStock(Double openingStock) {
        this.openingStock = openingStock;
    }

    public Integer getAlertQuantity() {
        return this.alertQuantity;
    }

    public Product alertQuantity(Integer alertQuantity) {
        this.alertQuantity = alertQuantity;
        return this;
    }

    public void setAlertQuantity(Integer alertQuantity) {
        this.alertQuantity = alertQuantity;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Product createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Product updatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return this.deletedAt;
    }

    public Product deletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Categorie getCategorie() {
        return this.categorie;
    }

    public Product categorie(Categorie categorie) {
        this.setCategorie(categorie);
        return this;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public Subcategorie getSubCategorie() {
        return this.subCategorie;
    }

    public Product subCategorie(Subcategorie subcategorie) {
        this.setSubCategorie(subcategorie);
        return this;
    }

    public void setSubCategorie(Subcategorie subcategorie) {
        this.subCategorie = subcategorie;
    }

    public Taxe getTaxe() {
        return this.taxe;
    }

    public Product taxe(Taxe taxe) {
        this.setTaxe(taxe);
        return this;
    }

    public void setTaxe(Taxe taxe) {
        this.taxe = taxe;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", quantity=" + getQuantity() +
            ", details='" + getDetails() + "'" +
            ", costPrice=" + getCostPrice() +
            ", minimumRetailPrice=" + getMinimumRetailPrice() +
            ", unit='" + getUnit() + "'" +
            ", status='" + getStatus() + "'" +
            ", image='" + getImage() + "'" +
            ", openingStock=" + getOpeningStock() +
            ", alertQuantity=" + getAlertQuantity() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            "}";
    }
}

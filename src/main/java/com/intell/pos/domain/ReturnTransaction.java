package com.intell.pos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReturnTransaction.
 */
@Entity
@Table(name = "return_transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReturnTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "return_vat", nullable = false)
    private Double returnVat;

    @NotNull
    @Size(max = 191)
    @Column(name = "sells_reference_no", length = 191, nullable = false)
    private String sellsReferenceNo;

    @NotNull
    @Column(name = "return_units", nullable = false)
    private Integer returnUnits;

    @NotNull
    @Column(name = "return_amount", nullable = false)
    private Double returnAmount;

    @NotNull
    @Column(name = "returned_by", nullable = false)
    private Integer returnedBy;

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
    @JsonIgnoreProperties(value = { "person", "product" }, allowSetters = true)
    private Sell sell;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReturnTransaction id(Long id) {
        this.id = id;
        return this;
    }

    public Double getReturnVat() {
        return this.returnVat;
    }

    public ReturnTransaction returnVat(Double returnVat) {
        this.returnVat = returnVat;
        return this;
    }

    public void setReturnVat(Double returnVat) {
        this.returnVat = returnVat;
    }

    public String getSellsReferenceNo() {
        return this.sellsReferenceNo;
    }

    public ReturnTransaction sellsReferenceNo(String sellsReferenceNo) {
        this.sellsReferenceNo = sellsReferenceNo;
        return this;
    }

    public void setSellsReferenceNo(String sellsReferenceNo) {
        this.sellsReferenceNo = sellsReferenceNo;
    }

    public Integer getReturnUnits() {
        return this.returnUnits;
    }

    public ReturnTransaction returnUnits(Integer returnUnits) {
        this.returnUnits = returnUnits;
        return this;
    }

    public void setReturnUnits(Integer returnUnits) {
        this.returnUnits = returnUnits;
    }

    public Double getReturnAmount() {
        return this.returnAmount;
    }

    public ReturnTransaction returnAmount(Double returnAmount) {
        this.returnAmount = returnAmount;
        return this;
    }

    public void setReturnAmount(Double returnAmount) {
        this.returnAmount = returnAmount;
    }

    public Integer getReturnedBy() {
        return this.returnedBy;
    }

    public ReturnTransaction returnedBy(Integer returnedBy) {
        this.returnedBy = returnedBy;
        return this;
    }

    public void setReturnedBy(Integer returnedBy) {
        this.returnedBy = returnedBy;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public ReturnTransaction createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public ReturnTransaction updatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return this.deletedAt;
    }

    public ReturnTransaction deletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Person getPerson() {
        return this.person;
    }

    public ReturnTransaction person(Person person) {
        this.setPerson(person);
        return this;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Sell getSell() {
        return this.sell;
    }

    public ReturnTransaction sell(Sell sell) {
        this.setSell(sell);
        return this;
    }

    public void setSell(Sell sell) {
        this.sell = sell;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReturnTransaction)) {
            return false;
        }
        return id != null && id.equals(((ReturnTransaction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReturnTransaction{" +
            "id=" + getId() +
            ", returnVat=" + getReturnVat() +
            ", sellsReferenceNo='" + getSellsReferenceNo() + "'" +
            ", returnUnits=" + getReturnUnits() +
            ", returnAmount=" + getReturnAmount() +
            ", returnedBy=" + getReturnedBy() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            "}";
    }
}

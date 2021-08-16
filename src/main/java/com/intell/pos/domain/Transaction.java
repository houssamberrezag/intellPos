package com.intell.pos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.intell.pos.domain.enumeration.TransactionTypes;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Transaction.
 */
@Entity
@Table(name = "transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 191)
    @Column(name = "reference_no", length = 191, nullable = false)
    private String referenceNo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionTypes transactionType;

    @Column(name = "total_cost_price")
    private Double totalCostPrice;

    @NotNull
    @Column(name = "discount", nullable = false)
    private Double discount;

    @NotNull
    @Column(name = "total", nullable = false)
    private Double total;

    @Column(name = "invoice_tax")
    private Double invoiceTax;

    @Column(name = "total_tax")
    private Double totalTax;

    @NotNull
    @Column(name = "labor_cost", nullable = false)
    private Double laborCost;

    @Column(name = "net_total")
    private Double netTotal;

    @NotNull
    @Column(name = "paid", nullable = false)
    private Double paid;

    @Column(name = "change_amount")
    private Double changeAmount;

    @Size(max = 191)
    @Column(name = "return_invoice", length = 191)
    private String returnInvoice;

    @Column(name = "return_balance")
    private Double returnBalance;

    @NotNull
    @Column(name = "pos", nullable = false)
    private Boolean pos;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Transaction id(Long id) {
        this.id = id;
        return this;
    }

    public String getReferenceNo() {
        return this.referenceNo;
    }

    public Transaction referenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
        return this;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public TransactionTypes getTransactionType() {
        return this.transactionType;
    }

    public Transaction transactionType(TransactionTypes transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    public void setTransactionType(TransactionTypes transactionType) {
        this.transactionType = transactionType;
    }

    public Double getTotalCostPrice() {
        return this.totalCostPrice;
    }

    public Transaction totalCostPrice(Double totalCostPrice) {
        this.totalCostPrice = totalCostPrice;
        return this;
    }

    public void setTotalCostPrice(Double totalCostPrice) {
        this.totalCostPrice = totalCostPrice;
    }

    public Double getDiscount() {
        return this.discount;
    }

    public Transaction discount(Double discount) {
        this.discount = discount;
        return this;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getTotal() {
        return this.total;
    }

    public Transaction total(Double total) {
        this.total = total;
        return this;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getInvoiceTax() {
        return this.invoiceTax;
    }

    public Transaction invoiceTax(Double invoiceTax) {
        this.invoiceTax = invoiceTax;
        return this;
    }

    public void setInvoiceTax(Double invoiceTax) {
        this.invoiceTax = invoiceTax;
    }

    public Double getTotalTax() {
        return this.totalTax;
    }

    public Transaction totalTax(Double totalTax) {
        this.totalTax = totalTax;
        return this;
    }

    public void setTotalTax(Double totalTax) {
        this.totalTax = totalTax;
    }

    public Double getLaborCost() {
        return this.laborCost;
    }

    public Transaction laborCost(Double laborCost) {
        this.laborCost = laborCost;
        return this;
    }

    public void setLaborCost(Double laborCost) {
        this.laborCost = laborCost;
    }

    public Double getNetTotal() {
        return this.netTotal;
    }

    public Transaction netTotal(Double netTotal) {
        this.netTotal = netTotal;
        return this;
    }

    public void setNetTotal(Double netTotal) {
        this.netTotal = netTotal;
    }

    public Double getPaid() {
        return this.paid;
    }

    public Transaction paid(Double paid) {
        this.paid = paid;
        return this;
    }

    public void setPaid(Double paid) {
        this.paid = paid;
    }

    public Double getChangeAmount() {
        return this.changeAmount;
    }

    public Transaction changeAmount(Double changeAmount) {
        this.changeAmount = changeAmount;
        return this;
    }

    public void setChangeAmount(Double changeAmount) {
        this.changeAmount = changeAmount;
    }

    public String getReturnInvoice() {
        return this.returnInvoice;
    }

    public Transaction returnInvoice(String returnInvoice) {
        this.returnInvoice = returnInvoice;
        return this;
    }

    public void setReturnInvoice(String returnInvoice) {
        this.returnInvoice = returnInvoice;
    }

    public Double getReturnBalance() {
        return this.returnBalance;
    }

    public Transaction returnBalance(Double returnBalance) {
        this.returnBalance = returnBalance;
        return this;
    }

    public void setReturnBalance(Double returnBalance) {
        this.returnBalance = returnBalance;
    }

    public Boolean getPos() {
        return this.pos;
    }

    public Transaction pos(Boolean pos) {
        this.pos = pos;
        return this;
    }

    public void setPos(Boolean pos) {
        this.pos = pos;
    }

    public Instant getDate() {
        return this.date;
    }

    public Transaction date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Transaction createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Transaction updatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return this.deletedAt;
    }

    public Transaction deletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Person getPerson() {
        return this.person;
    }

    public Transaction person(Person person) {
        this.setPerson(person);
        return this;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transaction)) {
            return false;
        }
        return id != null && id.equals(((Transaction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transaction{" +
            "id=" + getId() +
            ", referenceNo='" + getReferenceNo() + "'" +
            ", transactionType='" + getTransactionType() + "'" +
            ", totalCostPrice=" + getTotalCostPrice() +
            ", discount=" + getDiscount() +
            ", total=" + getTotal() +
            ", invoiceTax=" + getInvoiceTax() +
            ", totalTax=" + getTotalTax() +
            ", laborCost=" + getLaborCost() +
            ", netTotal=" + getNetTotal() +
            ", paid=" + getPaid() +
            ", changeAmount=" + getChangeAmount() +
            ", returnInvoice='" + getReturnInvoice() + "'" +
            ", returnBalance=" + getReturnBalance() +
            ", pos='" + getPos() + "'" +
            ", date='" + getDate() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            "}";
    }
}

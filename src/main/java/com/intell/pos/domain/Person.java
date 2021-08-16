package com.intell.pos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.intell.pos.domain.enumeration.PersonTypes;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Person.
 */
@Entity
@Table(name = "person")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 191)
    @Column(name = "first_name", length = 191, nullable = false)
    private String firstName;

    @Size(max = 191)
    @Column(name = "last_name", length = 191)
    private String lastName;

    @Size(max = 191)
    @Column(name = "company_name", length = 191)
    private String companyName;

    @Size(max = 191)
    @Column(name = "email", length = 191)
    private String email;

    @Size(max = 191)
    @Column(name = "phone", length = 191)
    private String phone;

    @Size(max = 191)
    @Column(name = "address", length = 191)
    private String address;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "person_type", nullable = false)
    private PersonTypes personType;

    @Column(name = "provious_due")
    private Double proviousDue;

    @Size(max = 191)
    @Column(name = "account_no", length = 191)
    private String accountNo;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @OneToMany(mappedBy = "person")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "person" }, allowSetters = true)
    private Set<Payment> payments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person id(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Person firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Person lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public Person companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return this.email;
    }

    public Person email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public Person phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return this.address;
    }

    public Person address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public PersonTypes getPersonType() {
        return this.personType;
    }

    public Person personType(PersonTypes personType) {
        this.personType = personType;
        return this;
    }

    public void setPersonType(PersonTypes personType) {
        this.personType = personType;
    }

    public Double getProviousDue() {
        return this.proviousDue;
    }

    public Person proviousDue(Double proviousDue) {
        this.proviousDue = proviousDue;
        return this;
    }

    public void setProviousDue(Double proviousDue) {
        this.proviousDue = proviousDue;
    }

    public String getAccountNo() {
        return this.accountNo;
    }

    public Person accountNo(String accountNo) {
        this.accountNo = accountNo;
        return this;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Person createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Person updatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return this.deletedAt;
    }

    public Person deletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Set<Payment> getPayments() {
        return this.payments;
    }

    public Person payments(Set<Payment> payments) {
        this.setPayments(payments);
        return this;
    }

    public Person addPayment(Payment payment) {
        this.payments.add(payment);
        payment.setPerson(this);
        return this;
    }

    public Person removePayment(Payment payment) {
        this.payments.remove(payment);
        payment.setPerson(null);
        return this;
    }

    public void setPayments(Set<Payment> payments) {
        if (this.payments != null) {
            this.payments.forEach(i -> i.setPerson(null));
        }
        if (payments != null) {
            payments.forEach(i -> i.setPerson(this));
        }
        this.payments = payments;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }
        return id != null && id.equals(((Person) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Person{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", companyName='" + getCompanyName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            ", personType='" + getPersonType() + "'" +
            ", proviousDue=" + getProviousDue() +
            ", accountNo='" + getAccountNo() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            "}";
    }
}

/**
 *
 */
package com.intell.pos.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author HP
 *
 */
@Entity
@Table(name = "settings")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Settings implements Serializable {

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
    @Column(name = "slogan", length = 191, nullable = false)
    private String slogan;

    @NotNull
    @Size(max = 191)
    @Column(name = "address", length = 191, nullable = false)
    private String address;

    @NotNull
    @Size(max = 191)
    @Column(name = "email", length = 191, nullable = false)
    private String email;

    @NotNull
    @Size(max = 191)
    @Column(name = "phone", length = 191, nullable = false)
    private String phone;

    public Settings(
        Long id,
        @NotNull @Size(max = 191) String name,
        @NotNull @Size(max = 191) String slogan,
        @NotNull @Size(max = 191) String address,
        @NotNull @Size(max = 191) String email,
        @NotNull @Size(max = 191) String phone
    ) {
        super();
        this.id = id;
        this.name = name;
        this.slogan = slogan;
        this.address = address;
        this.email = email;
        this.phone = phone;
    }

    public Settings() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return (
            "Settings [id=" +
            id +
            ", name=" +
            name +
            ", slogan=" +
            slogan +
            ", address=" +
            address +
            ", email=" +
            email +
            ", phone=" +
            phone +
            "]"
        );
    }
}

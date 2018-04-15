package com.demo.example.entities;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "ACCOUNT")
public class Account {




    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "email", unique = true, nullable = false)
    @NotNull
    @Email(message = "Wrong email format", regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    private String email;
    @Column(name = "accountName", unique = true, nullable = false)
    @NotNull
    private String accountName;
    @Column(name = "balance")
    private BigDecimal balance;

    @OneToMany(mappedBy = "accountFrom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Transfer> transfersFrom;

    public Set<Transfer> getTransfers() {
        return transfersFrom;
    }

    public void setTransfers(Set<Transfer> transfers) {
        this.transfersFrom = transfers;
    }

    @OneToMany(mappedBy = "accountTo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Transfer> transfersTo;

    public Set<Transfer> getTransfersTo() {
        return transfersTo;
    }

    public void setTransfersTo(Set<Transfer> transfersTo) {
        this.transfersTo = transfersTo;
    }

    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)

    private Date created = new Date();
    @Column(name = "lastUpdated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated = new Date();
    @Version
    private long version;

    @JsonIgnore
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }


    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }


    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}

package com.demo.example.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;


@Entity
@Table(name = "TRANSFER")
public class Transfer {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "accountFrom", nullable = false)
    private UUID accountFrom;

    @Column(name = "accountTo", nullable = false)
    private UUID accountTo;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created = new Date();
    @Version
    private long version;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(UUID accountFrom) {
        this.accountFrom = accountFrom;
    }

    public UUID getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(UUID accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}

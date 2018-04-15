package com.demo.example.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;


/**
 * The type Transfer.
 */
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

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created = new Date();
    @Version
    private long version;
    @ManyToOne
    @JoinColumn(name="account_from", nullable=false)
    private Account accountFrom;

    @ManyToOne
    @JoinColumn(name="account_to", nullable=false)
    private Account accountTo;


    /**
     * Gets account to.
     *
     * @return the account to
     */
    public Account getAccountTo() {
        return accountTo;
    }

    /**
     * Sets account to.
     *
     * @param accountTo the account to
     */
    public void setAccountTo(Account accountTo) {
        this.accountTo = accountTo;
    }

    /**
     * Gets account from.
     *
     * @return the account from
     */
    public Account getAccountFrom() {
        return accountFrom;
    }

    /**
     * Sets account from.
     *
     * @param accountFrom the account from
     */
    public void setAccountFrom(Account accountFrom) {
        this.accountFrom = accountFrom;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Gets balance.
     *
     * @return the balance
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Sets balance.
     *
     * @param balance the balance
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * Gets created.
     *
     * @return the created
     */
    public Date getCreated() {
        return created;
    }

    /**
     * Sets created.
     *
     * @param created the created
     */
    public void setCreated(Date created) {
        this.created = created;
    }
}

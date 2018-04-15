package com.demo.example.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;


/**
 * The type Transfer dto.
 */
public class TransferDto {

    private UUID id;

    private String accountFrom;

    private String accountTo;

    private BigDecimal balance;

    private Date created = new Date();

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

    /**
     * Gets account from.
     *
     * @return the account from
     */
    public String getAccountFrom() {
        return accountFrom;
    }

    /**
     * Sets account from.
     *
     * @param accountFrom the account from
     */
    public void setAccountFrom(String accountFrom) {
        this.accountFrom = accountFrom;
    }

    /**
     * Gets account to.
     *
     * @return the account to
     */
    public String getAccountTo() {
        return accountTo;
    }

    /**
     * Sets account to.
     *
     * @param accountTo the account to
     */
    public void setAccountTo(String accountTo) {
        this.accountTo = accountTo;
    }
}

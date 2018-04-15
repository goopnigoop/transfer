package com.demo.example.entities;

import java.util.List;

/**
 * The type Account transfers.
 *
 * @author aperevoz
 */
public class AccountTransfers {
    /**
     * The Outgoing transfers.
     */
    private List<Transfer> outgoingTransfers;
    /**
     * The Ingoing transfers.
     */
    private List<Transfer> ingoingTransfers;

    /**
     * Gets outgoing transfers.
     *
     * @return the outgoing transfers
     */
    public List<Transfer> getOutgoingTransfers() {
        return outgoingTransfers;
    }

    /**
     * Sets outgoing transfers.
     *
     * @param outgoingTransfers the outgoing transfers
     */
    public void setOutgoingTransfers(List<Transfer> outgoingTransfers) {
        this.outgoingTransfers = outgoingTransfers;
    }

    /**
     * Gets ingoing transfers.
     *
     * @return the ingoing transfers
     */
    public List<Transfer> getIngoingTransfers() {
        return ingoingTransfers;
    }

    /**
     * Sets ingoing transfers.
     *
     * @param ingoingTransfers the ingoing transfers
     */
    public void setIngoingTransfers(List<Transfer> ingoingTransfers) {
        this.ingoingTransfers = ingoingTransfers;
    }
}

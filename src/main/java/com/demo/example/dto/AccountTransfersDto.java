package com.demo.example.dto;

import com.demo.example.entities.Transfer;

import java.util.List;

/**
 * The type Account transfers dto.
 *
 * @author aperevoz
 */
public class AccountTransfersDto {
    /**
     * The Outgoing transfers.
     */
    private List<TransferDto> outgoingTransfers;
    /**
     * The Ingoing transfers.
     */
    private List<TransferDto> ingoingTransfers;

    /**
     * Gets outgoing transfers.
     *
     * @return the outgoing transfers
     */
    public List<TransferDto> getOutgoingTransfers() {
        return outgoingTransfers;
    }

    /**
     * Sets outgoing transfers.
     *
     * @param outgoingTransfers the outgoing transfers
     */
    public void setOutgoingTransfers(List<TransferDto> outgoingTransfers) {
        this.outgoingTransfers = outgoingTransfers;
    }

    /**
     * Gets ingoing transfers.
     *
     * @return the ingoing transfers
     */
    public List<TransferDto> getIngoingTransfers() {
        return ingoingTransfers;
    }

    /**
     * Sets ingoing transfers.
     *
     * @param ingoingTransfers the ingoing transfers
     */
    public void setIngoingTransfers(List<TransferDto> ingoingTransfers) {
        this.ingoingTransfers = ingoingTransfers;
    }
}

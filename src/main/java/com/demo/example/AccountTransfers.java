package com.demo.example;

import com.demo.example.dto.TransferDto;

import java.util.List;

/**
 * @author aperevoz
 */
public class AccountTransfers {
    List<TransferDto> outgoingTransfers;
    List<TransferDto> ingoingTransfers;

    public List<TransferDto> getOutgoingTransfers() {
        return outgoingTransfers;
    }

    public void setOutgoingTransfers(List<TransferDto> outgoingTransfers) {
        this.outgoingTransfers = outgoingTransfers;
    }

    public List<TransferDto> getIngoingTransfers() {
        return ingoingTransfers;
    }

    public void setIngoingTransfers(List<TransferDto> ingoingTransfers) {
        this.ingoingTransfers = ingoingTransfers;
    }
}

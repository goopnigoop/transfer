package com.demo.example.utils;

import com.demo.example.dto.TransferDto;
import com.demo.example.entities.Account;
import com.demo.example.exception.AppException;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * The type Check utils.
 */
public class CheckUtils {

    /**
     * Checkif object exists.
     *
     * @param id     the id
     * @param result the result
     * @throws AppException the app exception
     */
    public static void checkifObjectExists(String  id, Object result) throws AppException {
        if (Objects.isNull(result)) {
            throw new AppException(404, 301, "Entry is not found", String.format("Entry with property value %1$s is not found in database", id));
        }
    }

    /**
     * Check account balance.
     *
     * @param transfer    the transfer
     * @param accountFrom the account from
     * @throws AppException the app exception
     */
    public static void checkAccountBalance(BigDecimal transfer, Account accountFrom) throws AppException {
        if (accountFrom.getBalance().compareTo(transfer) < 0)
            throw new AppException(409, 307, "Transfer is impossible", String.format("AccountDto %s hasn't enough money for transfer operation",accountFrom.getId()));
    }


    /**
     * Check transfer.
     *
     * @param transfer the transfer
     * @throws AppException the app exception
     */
    public static void checkTransfer(TransferDto transfer) throws AppException {
        if (Objects.isNull(transfer.getBalance())) {
            throw new AppException(409, 301, "Transfer is impossible", "Transfer amount in transfer isn't set up");
        }
        if (Objects.isNull(transfer.getAccountFrom())) {
            throw new AppException(409, 302, "Transfer is impossible", "Field accountFrom must be set up");
        }
        if (transfer.getBalance().compareTo(BigDecimal.ZERO) <= 0) {
            throw new AppException(409, 303, "Transfer is impossible", "Transfer amount must be greater than 0");
        }
        if (transfer.getAccountFrom().equals(transfer.getAccountTo())) {
            throw new AppException(409, 304, "Transfer is impossible", "Account from and accountTo must be different");
        }
        if (Objects.isNull(transfer.getAccountTo())) {
            throw new AppException(409, 305, "Transfer is impossible", "Field accountTo must be set up");
        }
    }

}

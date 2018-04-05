package com.demo.example.utils;

import com.demo.example.dto.TransferDto;
import com.demo.example.entities.Account;
import com.demo.example.entities.Transfer;
import com.demo.example.exception.AppException;
import com.demo.example.services.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class CheckUtils {

    public static void checkifAccountExists(String  id, Account result) throws AppException {
        if (Objects.isNull(result)) {
            throw new AppException(404, 301, "Entry is not found", String.format("AccountDto with id %1$s is not found in database", id));
        }
    }

    public static void checkAccountBalance(Transfer transfer, Account accountFrom) throws AppException {
        if (accountFrom.getBalance().compareTo(transfer.getBalance()) < 0)
            throw new AppException(409, 302, "Transfer is impossible", String.format("AccountDto %s hasn't enough money for transfer operation",accountFrom.getId()));
    }


    public static void checkTransfer(TransferDto transfer) throws AppException {
        if (Objects.isNull(transfer.getBalance())) {
            throw new AppException(409, 303, "Transfer is impossible", "Transfer amount in transfer isn't set up");
        }
        if (Objects.isNull(transfer.getAccountFrom())) {
            throw new AppException(409, 303, "Transfer is impossible", "Field accountFrom must be set up");
        }
        if (transfer.getBalance().compareTo(BigDecimal.ZERO) <= 0) {
            throw new AppException(409, 304, "Transfer is impossible", "Transfer amount must be greater than 0");
        }
        if (transfer.getAccountFrom().equals(transfer.getAccountTo())) {
            throw new AppException(409, 304, "Transfer is impossible", "Account from and accountTo must be different");
        }
        if (Objects.isNull(transfer.getAccountTo())) {
            throw new AppException(409, 303, "Transfer is impossible", "Field accountTo must be set up");
        }
    }

}

package com.demo.example.services;

import com.demo.example.AccountTransfers;
import com.demo.example.utils.CheckUtils;
import com.demo.example.dao.AbstractDao;
import com.demo.example.dao.TransferDao;
import com.demo.example.dto.TransferDto;
import com.demo.example.entities.Account;
import com.demo.example.entities.Transfer;
import com.demo.example.exception.AppException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.StaleObjectStateException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.demo.example.utils.CheckUtils.checkAccountBalance;
import static com.demo.example.utils.CheckUtils.checkTransfer;
import static com.demo.example.services.SessionUtil.close;
import static com.demo.example.services.SessionUtil.open;

public class TransferService {

    private AbstractDao<Transfer, UUID> transferDao;

    private AbstractDao<Account, UUID> accountDao;

    private ModelMapper modelMapper;

    private final Logger logger = LogManager.getLogger(TransferService.class);

    public TransferService(AbstractDao<Transfer, UUID> transferDao, AbstractDao<Account, UUID> accountDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
    }

    public Transfer createTransfer(TransferDto transfer) throws AppException {
        logger.trace("Transfer preparation...");
        checkTransfer(transfer);
        logger.trace("Transfer's data is checked.");
        Transfer transferDB = modelMapper.map(transfer, Transfer.class);
        transferDB.setAccountFrom(UUID.fromString(transfer.getAccountFrom()));
        transferDB.setAccountTo(UUID.fromString(transfer.getAccountTo()));
        Account accountFrom;
        Account accountTo;
        checkTransferAccounts(transfer);

        while (true) {
            try {
                open(accountDao);
                accountFrom = accountDao.find(transferDB.getAccountFrom());
                accountTo = accountDao.find(transferDB.getAccountTo());
                checkAccountBalance(transferDB, accountFrom);
                accountFrom.setBalance(accountFrom.getBalance().subtract(transfer.getBalance()));
                accountTo.setBalance(accountTo.getBalance().add(transfer.getBalance()));
                close();
                open(accountDao);
                transferDao.setCurrentSession(SessionUtil.getSession());
                SessionUtil.beginTransaction();
                accountDao.update(accountFrom);
                accountDao.update(accountTo);
                SessionUtil.commitTransaction();
                SessionUtil.beginTransaction();
                transferDao.add(transferDB);
                SessionUtil.commitTransaction();
                break;
            } catch (StaleObjectStateException e) {
                SessionUtil.rollbackTransaction();
            } catch (AppException e) {
                throw e;
            } catch (Exception e) {
                SessionUtil.rollbackTransaction();
                throw e;
            } finally {
                SessionUtil.closeSession();
            }
        }
        return transferDB;
    }

    private void checkTransferAccounts(TransferDto transfer) throws AppException {
        Account accountFrom;
        Account accountTo;
        try {
            open(accountDao);
            accountFrom = accountDao.find(UUID.fromString(transfer.getAccountFrom()));
            CheckUtils.checkifAccountExists(transfer.getAccountFrom(), accountFrom);
            accountTo = accountDao.find(UUID.fromString(transfer.getAccountTo()));
            CheckUtils.checkifAccountExists(transfer.getAccountTo(), accountTo);
        } finally {
            close();
        }
    }

    public List<TransferDto> getListOfTransfers() {
        open(transferDao);
        List<Transfer> transferList = transferDao.list();
        List<TransferDto> transferDtoList = transferList.parallelStream().map(this::getTransferDto).collect(Collectors.toList());
        logger.info(String.format("%d transfers are found", transferList.size()));
        close();
        return transferDtoList;
    }


    private TransferDto getTransferDto(Transfer result) {
        return modelMapper.map(result, TransferDto.class);
    }

    private Transfer getTransfer(TransferDto transferDto) {
        return modelMapper.map(transferDto, Transfer.class);
    }

    public AccountTransfers getListOfTransfersforAccount(UUID uuid) {
        AccountTransfers accountTransfers = new AccountTransfers();
        try {
            open(transferDao);
            List<TransferDto> outgoingTransfers = ((TransferDao) transferDao).findByAccountFrom(uuid).stream().map(this::getTransferDto).collect(Collectors.toList());
            logger.trace(String.format("For account %1$s %2$s outgoing transfers were found",uuid.toString(),outgoingTransfers.size()));
            accountTransfers.setOutgoingTransfers(outgoingTransfers);
            List<TransferDto> ingoingTransfers = ((TransferDao) transferDao).findByAccountTo(uuid).stream().map(this::getTransferDto).collect(Collectors.toList());
            accountTransfers.setIngoingTransfers(ingoingTransfers);
            logger.trace(String.format("For account %1$s %2$s ingoing transfers were found",uuid.toString(),ingoingTransfers.size()));
            return accountTransfers;
        } finally {
            close();
        }
    }


    public TransferDto getTransferByUuid(UUID uuid) {
        return getTransferDto(transferDao.find(uuid));
    }
}

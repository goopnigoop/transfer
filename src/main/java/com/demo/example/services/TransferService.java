package com.demo.example.services;

import com.demo.example.dao.AbstractDao;
import com.demo.example.dao.TransferDao;
import com.demo.example.dto.TransferDto;
import com.demo.example.entities.Account;
import com.demo.example.entities.AccountTransfers;
import com.demo.example.entities.Transfer;
import com.demo.example.exception.AppException;
import com.demo.example.utils.CheckUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.StaleObjectStateException;

import java.util.List;
import java.util.UUID;

import static com.demo.example.utils.CheckUtils.*;

/**
 * The type Transfer service.
 */
public class TransferService {

    private AbstractDao<Transfer, UUID> transferDao;

    private AbstractDao<Account, UUID> accountDao;

    private GenericService<Transfer, UUID> genericService;

    private SessionService sessionService;

    private final Logger logger = LogManager.getLogger(TransferService.class);

    /**
     * Instantiates a new Transfer service.
     *
     * @param transferDao the transfer dao
     * @param accountDao  the account dao
     */
    public TransferService(AbstractDao<Transfer, UUID> transferDao, AbstractDao<Account, UUID> accountDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
        sessionService = new SessionService();
        genericService = new GenericServiceImpl<>(transferDao, sessionService);
    }

    /**
     * Create transfer transfer.
     *
     * @param transfer the transfer
     * @return the transfer
     * @throws AppException the app exception
     */
    public Transfer createTransfer(TransferDto transfer) throws AppException {
        Transfer result;
        logger.trace("Transfer preparation...");
        checkTransfer(transfer);
        logger.trace("Transfer's data is checked.");
        while (true) {
            try {
                sessionService.setSessionForDao(accountDao);
                Transfer transferWithAccounts = checkTransferAccounts(transfer);
                Account accountFrom = transferWithAccounts.getAccountFrom();
                Account accountTo = transferWithAccounts.getAccountTo();

                checkAccountBalance(transfer.getBalance(), accountFrom);
                accountFrom.setBalance(accountFrom.getBalance().subtract(transfer.getBalance()));
                accountTo.setBalance(accountTo.getBalance().add(transfer.getBalance()));
                result = doTransfer(transferWithAccounts, accountFrom, accountTo);
                break;
            } catch (StaleObjectStateException e) {
                sessionService.rollback();
            } catch (AppException e) {
                throw e;
            } catch (Exception e) {
                sessionService.rollback();
                throw e;
            } finally {
                sessionService.closeSession();
            }
        }
        return result;
    }

    private Transfer doTransfer(Transfer transferWithAccounts, Account accountFrom, Account accountTo) {
        sessionService.begin(transferDao);
        accountDao.update(accountFrom);
        accountDao.update(accountTo);
        Transfer result = transferDao.add(transferWithAccounts);
        sessionService.commit();
        return result;
    }

    private Transfer checkTransferAccounts(TransferDto transfer) throws AppException {
        Account accountFrom = accountDao.find(UUID.fromString(transfer.getAccountFrom()));
        CheckUtils.checkifObjectExists(transfer.getAccountFrom(), accountFrom);
        Account accountTo = accountDao.find(UUID.fromString(transfer.getAccountTo()));
        CheckUtils.checkifObjectExists(transfer.getAccountTo(), accountTo);
        Transfer transferDB = new Transfer();
        transferDB.setAccountFrom(accountFrom);
        transferDB.setAccountTo(accountTo);
        transferDB.setBalance(transfer.getBalance());
        return transferDB;

    }

    /**
     * Gets list of transfers.
     *
     * @return the list of transfers
     */
    public List<Transfer> getListOfTransfers() {
        try {
            sessionService.setSessionForDao(transferDao);
            List<Transfer> transferList = transferDao.list();
            logger.info(String.format("%d transfers are found", transferList.size()));
            return transferList;
        } finally {
            sessionService.close();
        }

    }

    /**
     * Gets list of transfers for account.
     *
     * @param uuid the uuid
     * @return the list of transfers for account
     * @throws AppException the app exception
     */
    public AccountTransfers getListOfTransfersForAccount(UUID uuid) throws AppException {
        AccountTransfers accountTransfers = new AccountTransfers();
        try {
            checkAccount(uuid);
/*            sessionService.openSession();*/
            sessionService.setSessionForDao(transferDao);
            List<Transfer> outgoingTransfers = ((TransferDao) transferDao).findByAccountFrom(uuid);
            logger.trace(String.format("For account %1$s %2$s outgoing transfers were found", uuid.toString(), outgoingTransfers.size()));
            accountTransfers.setOutgoingTransfers(outgoingTransfers);
            List<Transfer> ingoingTransfers = ((TransferDao) transferDao).findByAccountTo(uuid);
            accountTransfers.setIngoingTransfers(ingoingTransfers);
            logger.trace(String.format("For account %1$s %2$s ingoing transfers were found", uuid.toString(), ingoingTransfers.size()));
            return accountTransfers;
        } finally {
            sessionService.close();
        }
    }

    private void checkAccount(UUID uuid) throws AppException {
        accountDao.setCurrentSession(sessionService.getSession());
        checkifObjectExists(uuid.toString(),accountDao.find(uuid));
    }


    /**
     * Gets transfer by uuid.
     *
     * @param uuid the uuid
     * @return the transfer by uuid
     * @throws AppException the app exception
     */
    public Transfer getTransferByUuid(UUID uuid) throws AppException {
        Transfer account = genericService.getEntity(uuid);
        logger.info(String.format("transfer with UUID =$1%s is found in database", uuid));
        return account;
    }
}

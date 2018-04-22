package com.demo.example.services;

import com.demo.example.dao.AbstractDao;
import com.demo.example.dao.AccountDaoImpl;
import com.demo.example.entities.Account;
import com.demo.example.exception.AppException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.demo.example.utils.CheckUtils.checkifObjectExists;

/**
 * The type AccountDto service.
 */
public class AccountService {

    private GenericService<Account, UUID> genericService;

    private AbstractDao<Account, UUID> abstractDAO;

    private SessionService sessionService;

    private final Logger logger = LogManager.getLogger(AccountService.class);

    /**
     * Instantiates a new AccountDto service.
     *
     * @param accountDAO the account dao
     */
    public AccountService(AbstractDao<Account, UUID> accountDAO) {
        this.abstractDAO = accountDAO;
        sessionService = new SessionService();
        genericService = new GenericServiceImpl<>(accountDAO, sessionService);

    }

    /**
     * Save account account.
     *
     * @param account the account
     * @return the account
     */
    public Account saveAccount(Account account) {
        if(account.getBalance()==null) account.setBalance(BigDecimal.ZERO);
        Account result = genericService.saveEntity(account);
        logger.info(String.format("New entity with UUID = %1$s is created", result.getId()));
        return result;
    }


    /**
     * Gets account.
     *
     * @param id the id
     * @return the account
     * @throws AppException the app exception
     */
    public Account getAccount(UUID id) throws AppException {
        Account account = genericService.getEntity(id);
        logger.info(String.format("Account with UUID = %1$s is found in database", account.getId()));
        return account;
    }


    /**
     * Update account.
     *
     * @param account the account
     * @param id      the id
     * @throws AppException the app exception
     */
    public void updateAccount(Account account, UUID id) throws AppException {
        try {
            sessionService.begin(abstractDAO);
            Account accountFodDB = getAccount(account, id);
            abstractDAO.update(accountFodDB);
            sessionService.commit();
            logger.info(String.format("Account with UUID = %1$s is successfully updated ", id));
        } finally {
            sessionService.close();
        }
    }

    private Account getAccount(Account account, UUID id) throws AppException {
        Account currentAccount = abstractDAO.find(id);
        checkifObjectExists(id.toString(), currentAccount);
        String accountName = Objects.isNull(account.getAccountName()) ? currentAccount.getAccountName() : account.getAccountName();
        String email = Objects.isNull(account.getEmail()) ? currentAccount.getEmail() : account.getEmail();
        BigDecimal balance = Objects.isNull(account.getBalance()) ? currentAccount.getBalance() : account.getBalance();
        currentAccount.setAccountName(accountName);
        currentAccount.setBalance(balance);
        currentAccount.setEmail(email);
        currentAccount.setUpdated(new Date());
        return currentAccount;
    }


    /**
     * Delete account.
     *
     * @param id the id
     * @throws AppException the app exception
     */
    public void deleteAccount(UUID id) throws AppException {
        genericService.deleteEntity(id);
        logger.info(String.format("Account with UUID = %1$s is successfully deleted ", id));
    }


    /**
     * Gets list of accounts.
     *
     * @param email     the email
     * @param account   the account
     * @param startDate the start date
     * @param endDate   the end date
     * @return the list of accounts
     * @throws ParseException the parse exception
     */
    public List<Account> getListOfAccounts(String email, String account, String startDate, String endDate) throws ParseException {
        List<Account> accountList;
        if (Objects.isNull(email) && Objects.isNull(account) && Objects.isNull(startDate) &&Objects.isNull(endDate)) {
            accountList = genericService.getlistOfEntities();
        } else {
            accountList = getAccountByParameters(email,account,startDate,endDate);
        }
        logger.info(String.format("%d accounts are found", accountList.size()));
        return accountList;
    }

    private List<Account> getAccountByParameters(String email, String account, String startDate, String endDate) throws ParseException {
        try {
            sessionService.setSessionForDao(abstractDAO);
            List<Account> accountList = ((AccountDaoImpl) abstractDAO).getAccountByParameters(email,account,startDate,endDate);
            return accountList;
        } finally {
            sessionService.close();
        }
    }

}

package com.demo.example.services;

import com.demo.example.dao.AbstractDao;
import com.demo.example.dto.AccountDto;
import com.demo.example.entities.Account;
import com.demo.example.exception.AppException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.demo.example.utils.CheckUtils.checkifAccountExists;
import static com.demo.example.services.SessionUtil.*;

/**
 * The type AccountDto service.
 */
public class AccountService {

    /**
     * The Abstract dao.
     */
    private AbstractDao<Account, UUID> abstractDAO;

    private ModelMapper modelMapper;


    private final Logger logger = LogManager.getLogger(AccountService .class);

    /**
     * Instantiates a new AccountDto service.
     *
     * @param accountDAO the account dao
     */
    public AccountService(AbstractDao<Account, UUID> accountDAO) {
        this.abstractDAO = accountDAO;
        modelMapper = new ModelMapper();
    }

    /**
     * Save account account.
     *
     * @param account the account
     * @return the account
     */
    public AccountDto saveAccount(AccountDto account) {
        Account accountEntity = getAccount(account);
        try {
            begin(abstractDAO);
            Account result = abstractDAO.add(accountEntity);
            commit();
            logger.info(String.format("New account with UUID =$1%s is created", result.getId()));
            return getAccountDto(result);
        } catch (Exception e) {
            rollback();
            throw e;
        } finally {
            close();
        }
    }


    /**
     * Gets account.
     *
     * @param id the id
     * @return the account
     * @throws AppException the app exception
     */
    public AccountDto getAccount(UUID id) throws AppException {
        try {
            open(abstractDAO);
            Account result = abstractDAO.find(id);
            checkifAccountExists(id.toString(), result);
            logger.info(String.format("New account with UUID =$1%s is found in database", result.getId()));
            return getAccountDto(result);
        } finally {
            close();
        }
    }


    /**
     * Update account.
     *
     * @param newAccount the new account
     * @param id         the id
     * @throws AppException the app exception
     */
    public void updateAccount(AccountDto newAccount, UUID id) throws AppException {
        try {
            begin(abstractDAO);
            Account accountFodDB = getAccount(newAccount, id);
            abstractDAO.update(accountFodDB);
            commit();
            logger.info(String.format("Account with UUID =$1%s is successfully updated ", id));
        } finally {
            close();
        }
    }

    private Account getAccount(AccountDto newAccount, UUID id) throws AppException {
        Account currentAccount = abstractDAO.find(id);
        checkifAccountExists(id.toString(), currentAccount);
        String accountName = Objects.isNull(newAccount.getAccountName()) ? currentAccount.getAccountName() : newAccount.getAccountName();
        String email = Objects.isNull(newAccount.getEmail()) ? currentAccount.getEmail() : newAccount.getEmail();
        BigDecimal balance = Objects.isNull(newAccount.getBalance()) ? currentAccount.getBalance() : newAccount.getBalance();
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
        try {
            AccountDto account = getAccount(id);
            begin(abstractDAO);
            abstractDAO.remove(getAccount(account));
            commit();
            logger.info(String.format("Account with UUID =$1%s is successfully deleted ", id));
        } finally {
            close();
        }
    }


    private AccountDto getAccountDto(Account result) {
        return modelMapper.map(result, AccountDto.class);
    }

    private Account getAccount(AccountDto account) {
        return modelMapper.map(account, Account.class);
    }

    public List<AccountDto> getListOfAccounts() {
        open(abstractDAO);
        List<Account> accountList = abstractDAO.list();
        List<AccountDto> result = accountList.parallelStream().map(this::getAccountDto).collect(Collectors.toList());
        logger.info(String.format("%d accounts are found", result.size()));
        close();
        return result;
    }
}

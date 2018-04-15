package com.demo.example;

import com.demo.example.dao.AbstractDao;
import com.demo.example.dao.AccountDaoImpl;
import com.demo.example.dto.AccountDto;
import com.demo.example.entities.Account;
import com.demo.example.exception.AppException;
import com.demo.example.exception.ExceptionHandler;
import com.demo.example.services.AccountService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * The type Account resource.
 */
@Path("account")
public class AccountResource extends AbstractResource<Account, AccountDto> {

    private AccountService accountService;

    /**
     * Instantiates a new Account resource.
     */
    public AccountResource() {
        AbstractDao<Account, UUID> abstractDAO = new AccountDaoImpl();
        this.accountService = new AccountService(abstractDAO);
    }


    /**
     * Create account account dto.
     *
     * @param accountDto the account dto
     * @return the account dto
     * @throws AppException the app exception
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(AccountDto accountDto) throws AppException {
        try {
            Account account = getByDto(accountDto);
            AccountDto accountResult = getDto(accountService.saveAccount(account));
            return Response.created(URI.create(accountResult.getId().toString())).entity(accountResult).build();
        } catch (Exception e) {
            throw ExceptionHandler.handle(e);
        }
    }

    /**
     * Gets accounts.
     *
     * @param email     the email
     * @param startDate the start date
     * @param endDate   the end date
     * @param account   the account
     * @return the accounts
     * @throws AppException the app exception
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<AccountDto> getAccounts(@QueryParam("email") String email,
                                        @QueryParam("startDate") String startDate,
                                        @QueryParam("endDate") String endDate,
                                        @QueryParam("accountName") String account) throws AppException {
        try {
            List<Account> accountList = accountService.getListOfAccounts(email,account,startDate,endDate);
            return accountList.parallelStream().map(this::getDto).collect(Collectors.toList());
        } catch (Exception e) {
            throw ExceptionHandler.handle(e);
        }
    }

    /**
     * Gets account.
     *
     * @param id the id
     * @return the account
     * @throws AppException the app exception
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public AccountDto getAccount(@PathParam("id") UUID id) throws AppException {
        try {
            Account result = accountService.getAccount(id);
            return getDto(result);
        } catch (Exception e) {
            throw ExceptionHandler.handle(e);
        }
    }

    /**
     * Update account.
     *
     * @param id      the id
     * @param account the account
     * @throws AppException the app exception
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public void updateAccount(@PathParam("id") UUID id, AccountDto account) throws AppException {
        try {
            accountService.updateAccount(getByDto(account), id);
        } catch (Exception e) {
            throw ExceptionHandler.handle(e);
        }
    }

    /**
     * Delete account.
     *
     * @param id the id
     * @throws AppException the app exception
     */
    @DELETE
    @Path("/{id}")
    public void deleteAccount(@PathParam("id") UUID id) throws AppException {
        try {
            accountService.deleteAccount(id);
        } catch (Exception e) {
            throw ExceptionHandler.handle(e);
        }
    }


}

package com.demo.example;

import com.demo.example.dao.AbstractDao;
import com.demo.example.dao.AccountDaoImpl;
import com.demo.example.dto.AccountDto;
import com.demo.example.entities.Account;
import com.demo.example.exception.AppException;
import com.demo.example.exception.ExceptionHandler;
import com.demo.example.services.AccountService;
import org.modelmapper.ModelMapper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;


@Path("account")
public class AccountResource {

    private AccountService accountService;

    public AccountResource() {
        AbstractDao<Account, UUID> abstractDAO = new AccountDaoImpl();
        this.accountService = new AccountService(abstractDAO);
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AccountDto createAccount(AccountDto account) throws AppException {
        try {
            return accountService.saveAccount(account);
        } catch (Exception e) {
            throw ExceptionHandler.handle(e);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<AccountDto> getAccounts() throws AppException {
        try {
            return accountService.getListOfAccounts();
        } catch (Exception e){
            throw ExceptionHandler.handle(e);
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public AccountDto getAccount(@PathParam("id") UUID id) throws AppException {
        try {
            return accountService.getAccount(id);
        } catch (Exception e) {
            throw ExceptionHandler.handle(e);
        }

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public void updateAccount(@PathParam("id") UUID id, AccountDto account) throws AppException {
        try {
            accountService.updateAccount(account, id);
        } catch (Exception e){
            throw  ExceptionHandler.handle(e);
        }
    }

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

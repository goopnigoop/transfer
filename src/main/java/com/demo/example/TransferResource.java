package com.demo.example;

import com.demo.example.dao.AbstractDao;
import com.demo.example.dao.AccountDaoImpl;
import com.demo.example.dao.TransferDao;
import com.demo.example.dto.TransferDto;
import com.demo.example.entities.Account;
import com.demo.example.entities.Transfer;
import com.demo.example.exception.AppException;
import com.demo.example.exception.ExceptionHandler;
import com.demo.example.services.TransferService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;

@Path("transfer")
public class TransferResource {

    TransferService transferService;

    public TransferResource() {
        AbstractDao<Transfer, UUID> transferDao = new TransferDao();
        AbstractDao<Account, UUID> accountDao = new AccountDaoImpl();
        transferService = new TransferService(transferDao, accountDao);

    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Transfer createTransfer(TransferDto transfer) throws AppException {
        try {
            return transferService.createTransfer(transfer);
        } catch (Exception e) {
            throw ExceptionHandler.handle(e);
        }
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TransferDto> getTransfers() throws AppException {
        try {
            return transferService.getListOfTransfers();
        } catch (Exception e){
            throw ExceptionHandler.handle(e);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public TransferDto getTransferById(@PathParam("id") UUID uuid) throws AppException {
        try {
            return transferService.getTransferByUuid(uuid);
        } catch (Exception e){
            throw ExceptionHandler.handle(e);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/account/{id}")
    public AccountTransfers getTransfersForAccount(@PathParam("id") UUID uuid) throws AppException {
        try {
            return transferService.getListOfTransfersforAccount(uuid);
        } catch (Exception e){
            throw ExceptionHandler.handle(e);
        }
    }





}




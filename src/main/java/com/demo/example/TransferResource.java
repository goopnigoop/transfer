package com.demo.example;

import com.demo.example.dao.AbstractDao;
import com.demo.example.dao.AccountDaoImpl;
import com.demo.example.dao.TransferDao;
import com.demo.example.dto.AccountTransfersDto;
import com.demo.example.dto.TransferDto;
import com.demo.example.entities.Account;
import com.demo.example.entities.AccountTransfers;
import com.demo.example.entities.Transfer;
import com.demo.example.exception.AppException;
import com.demo.example.exception.ExceptionHandler;
import com.demo.example.services.TransferService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The type Transfer resource.
 */
@Path("transfer")
public class TransferResource extends AbstractResource<Transfer, TransferDto> {

    private TransferService transferService;

    /**
     * Instantiates a new Transfer resource.
     */
    public TransferResource() {
        AbstractDao<Transfer, UUID> transferDao = new TransferDao();
        AbstractDao<Account, UUID> accountDao = new AccountDaoImpl();
        transferService = new TransferService(transferDao, accountDao);
    }

    /**
     * Create transfer transfer dto.
     *
     * @param transfer the transfer
     * @return the transfer dto
     * @throws AppException the app exception
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTransfer(TransferDto transfer) throws AppException {
        try {
            TransferDto result = getDto(transferService.createTransfer(transfer));
            return Response.created(URI.create(result.getId().toString())).entity(result).build();
        } catch (Exception e) {
            throw ExceptionHandler.handle(e);
        }
    }

    /**
     * Gets transfers.
     *
     * @return the transfers
     * @throws AppException the app exception
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TransferDto> getTransfers() throws AppException {
        try {
            return transferService.getListOfTransfers().parallelStream().map(this::getDto).collect(Collectors.toList());
        } catch (Exception e) {
            throw ExceptionHandler.handle(e);
        }
    }

    /**
     * Gets transfer by id.
     *
     * @param uuid the uuid
     * @return the transfer by id
     * @throws AppException the app exception
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public TransferDto getTransferById(@PathParam("id") UUID uuid) throws AppException {
        try {
            return getDto(transferService.getTransferByUuid(uuid));
        } catch (Exception e) {
            throw ExceptionHandler.handle(e);
        }
    }

    /**
     * Gets transfers for account.
     *
     * @param uuid the uuid
     * @return the transfers for account
     * @throws AppException the app exception
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/account/{id}")
    public AccountTransfersDto getTransfersForAccount(@PathParam("id") UUID uuid) throws AppException {
        try {
            AccountTransfers accountTransfers = transferService.getListOfTransfersForAccount(uuid);
            AccountTransfersDto accountTransfersDto = new AccountTransfersDto();
            accountTransfersDto.setIngoingTransfers(accountTransfers.getIngoingTransfers().parallelStream().map(this::getDto).collect(Collectors.toList()));
            accountTransfersDto.setOutgoingTransfers(accountTransfers.getOutgoingTransfers().parallelStream().map(this::getDto).collect(Collectors.toList()));
            return accountTransfersDto;
        } catch (Exception e) {
            throw ExceptionHandler.handle(e);
        }
    }


    @Override
    public TransferDto getDto(Transfer transfer) {
        TransferDto transferDto = new TransferDto();
        transferDto.setAccountFrom(transfer.getAccountFrom().getId().toString());
        transferDto.setAccountTo(transfer.getAccountTo().getId().toString());
        transferDto.setBalance(transfer.getBalance());
        transferDto.setId(transfer.getId());
        transferDto.setCreated(transfer.getCreated());
        return transferDto;
    }

}




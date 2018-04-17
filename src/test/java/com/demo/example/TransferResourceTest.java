package com.demo.example;

import com.demo.example.dto.AccountDto;
import com.demo.example.dto.TransferDto;
import com.demo.example.exception.AppExceptionMapper;
import com.jayway.restassured.http.ContentType;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Test;

import javax.ws.rs.core.Application;
import java.math.BigDecimal;
import java.util.UUID;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class TransferResourceTest extends BaseTest {

    private static final BigDecimal BIG_DECIMAL_ERROR = new BigDecimal(0.0001);
    private AccountDto accountDtoFrom;

    private AccountDto accountDtoTo;

    private TransferDto transferDto;

    private String idFrom;
    private String idTo;

    @Override
    protected Application configure() {
        return new ResourceConfig(TransferResource.class, AccountResource.class, AppExceptionMapper.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        accountDtoFrom = new AccountDto();
        accountDtoFrom.setAccountName("accountFrom");
        accountDtoFrom.setBalance(new BigDecimal(1000.5));
        accountDtoFrom.setEmail("accountFrom@google.com");

        accountDtoTo = new AccountDto();
        accountDtoTo.setAccountName("accountTo");
        accountDtoTo.setEmail("accountTo@google.com");
        accountDtoTo.setBalance(new BigDecimal(100.27));

        idFrom = given().contentType("application/json")
                .body(accountDtoFrom).post("/account").then().statusCode(201).extract().jsonPath().getString("id");
        idTo = given().contentType("application/json")
                .body(accountDtoTo).post("/account").then().statusCode(201).extract().jsonPath().getString("id");

        transferDto = new TransferDto();
        transferDto.setBalance(new BigDecimal(100));
        transferDto.setAccountFrom(idFrom);
        transferDto.setAccountTo(idTo);
    }

    @Override
    public void tearDown() throws Exception {
        try {
            given().contentType(ContentType.JSON)
                    .pathParam("id", UUID.fromString(idFrom))
                    .delete("/account/{id}")
                    .then()
                    .statusCode(204);

            given().contentType(ContentType.JSON)
                    .pathParam("id", UUID.fromString(idTo))
                    .delete("/account/{id}")
                    .then()
                    .statusCode(204);
        } finally {
            super.tearDown();
        }
    }

    @Test
    public void testCreateTransfer() {
        given().contentType("application/json")
                .body(transferDto)
                .when().post("/transfer")
                .then().statusCode(201)
                .body("accountFrom", equalTo(idFrom))
                .body("accountTo", equalTo(idTo))
                .body("balance", equalTo(transferDto.getBalance().intValue()))
                .body("id", isA(String.class))
                .body("created", notNullValue());

        BigDecimal balanceFrom = new BigDecimal(given().contentType("application/json").pathParam("id", UUID.fromString(idFrom))
                .when()
                .get("/account/{id}").then().statusCode(200).extract().jsonPath().getString("balance"));

        BigDecimal balanceTo = new BigDecimal(given().contentType("application/json").pathParam("id", UUID.fromString(idTo))
                .when()
                .get("/account/{id}").then().statusCode(200).extract().jsonPath().getString("balance"));

        assertThat(balanceFrom, closeTo(accountDtoFrom.getBalance().subtract(transferDto.getBalance()), new BigDecimal(0.00001)));
        assertThat(balanceTo, closeTo(accountDtoTo.getBalance().add(transferDto.getBalance()), new BigDecimal(0.00001)));
    }

    @Test
    public void testTransferList() {
        BigDecimal balanceAmount = new BigDecimal(100);
        transferDto.setBalance(balanceAmount);
        int quantityOfTransfers = 10;
        for (int i = 0; i < quantityOfTransfers; i++) {
            given().contentType("application/json")
                    .body(transferDto)
                    .when().post("/transfer")
                    .then().statusCode(201)
                    .body("accountFrom", equalTo(idFrom))
                    .body("accountTo", equalTo(idTo))
                    .body("balance", equalTo(transferDto.getBalance().intValue()))
                    .body("id", isA(String.class))
                    .body("created", notNullValue());
        }
        given().contentType(ContentType.JSON)
                .when()
                .get("/transfer")
                .then()
                .statusCode(200)
                .body("size()", is(quantityOfTransfers));
    }

    @Test
    public void testTransferById() {
        TransferDto transferDto = new TransferDto();
        transferDto.setBalance(new BigDecimal(100));
        transferDto.setAccountFrom(idFrom);
        transferDto.setAccountTo(idTo);

        String id = given().contentType("application/json")
                .body(transferDto).post("/transfer").then().statusCode(201).extract().jsonPath().getString("id");

        given().contentType(ContentType.JSON)
                .pathParam("id", UUID.fromString(id))
                .when()
                .get("/transfer/{id}")
                .then()
                .statusCode(200)
                .body("accountFrom", equalTo(idFrom))
                .body("accountTo", equalTo(idTo));
    }

    @Test
    public void testMultipleTransfers() {
        BigDecimal balanceAmount = new BigDecimal(100);
        transferDto.setBalance(balanceAmount);
        int quantityOfTransfers = 10;
        for (int i = 0; i < quantityOfTransfers; i++) {
            given().contentType("application/json")
                    .body(transferDto)
                    .when().post("/transfer")
                    .then().statusCode(201)
                    .body("accountFrom", equalTo(idFrom))
                    .body("accountTo", equalTo(idTo));
        }
        BigDecimal balanceFrom = new BigDecimal(given().contentType("application/json").pathParam("id", UUID.fromString(idFrom))
                .when()
                .get("/account/{id}").then().statusCode(200).extract().jsonPath().getString("balance"));

        BigDecimal balanceTo = new BigDecimal(given().contentType("application/json").pathParam("id", UUID.fromString(idTo))
                .when()
                .get("/account/{id}").then().statusCode(200).extract().jsonPath().getString("balance"));

        assertThat(balanceFrom, closeTo(accountDtoFrom.getBalance().subtract(balanceAmount.multiply(BigDecimal.TEN)), BIG_DECIMAL_ERROR));
        assertThat(balanceTo, closeTo(accountDtoTo.getBalance().add(balanceAmount.multiply(BigDecimal.TEN)), BIG_DECIMAL_ERROR));
    }

    @Test
    public void testTransfersAccountsDto() {
        BigDecimal balanceAmount = new BigDecimal(78.437);
        transferDto.setBalance(balanceAmount);
        int quantityOfTransfers = 10;
        for (int i = 0; i < quantityOfTransfers; i++) {
            given().contentType("application/json")
                    .body(transferDto)
                    .when().post("/transfer")
                    .then().statusCode(201);
        }

        String accountTo = transferDto.getAccountTo();
        transferDto.setAccountTo(transferDto.getAccountFrom());
        transferDto.setAccountFrom(accountTo);

        for (int i = 0; i < quantityOfTransfers; i++) {
            given().contentType("application/json")
                    .body(transferDto)
                    .when().post("/transfer")
                    .then().statusCode(201);
        }

        given().contentType(ContentType.JSON)
                .pathParam("id", UUID.fromString(idFrom))
                .when()
                .get("/transfer/account/{id}")
                .then()
                .statusCode(200)
                .body("size()",is(2))
                .body("outgoingTransfers.size()",is(quantityOfTransfers))
                .body("ingoingTransfers.size()",is(quantityOfTransfers));
    }


    @Test
    public void testMultipleTransfersBalanceAmountIsNotEnoughFailed() {
        BigDecimal balanceAmount = new BigDecimal(100);
        transferDto.setBalance(balanceAmount);
        int quantityOfTransfers = 10;
        for (int i = 0; i < quantityOfTransfers; i++) {
            given().contentType("application/json")
                    .body(transferDto)
                    .when().post("/transfer")
                    .then().statusCode(201)
                    .body("accountFrom", equalTo(idFrom))
                    .body("accountTo", equalTo(idTo));
        }
        transferDto.setBalance(new BigDecimal(0.6));
        given().contentType("application/json")
                .body(transferDto)
                .when().post("/transfer")
                .then().statusCode(409)
                .body("developerMessage", is(String.format("AccountDto %s hasn't enough money for transfer operation", idFrom)))
                .body("code", is(307));
    }

    @Test
    public void testCreateTransferDuplicateAccountFailed() {
        transferDto.setAccountTo(idFrom);
        given().contentType("application/json")
                .body(transferDto)
                .when().post("/transfer")
                .then().statusCode(409)
                .body("developerMessage", is("Account from and accountTo must be different"))
                .body("code", is(304));
    }

    @Test
    public void testCreateTransferBalanceIsNotSetUpFailed() {
        transferDto.setBalance(null);
        given().contentType("application/json")
                .body(transferDto)
                .when().post("/transfer")
                .then().statusCode(409)
                .body("developerMessage", is("Transfer amount in transfer isn't set up"))
                .body("code", is(301));
    }

    @Test
    public void testCreateTransferAccountFromIsNotSetUpFailed() {
        transferDto.setAccountFrom(null);
        given().contentType("application/json")
                .body(transferDto)
                .when().post("/transfer")
                .then().statusCode(409)
                .body("developerMessage", is("Field accountFrom must be set up"))
                .body("code", is(302));
    }

    @Test
    public void testCreateTransferBalanceIsNegativeFailed() {
        transferDto.setBalance(new BigDecimal(-100));
        given().contentType("application/json")
                .body(transferDto)
                .when().post("/transfer")
                .then().statusCode(409)
                .body("developerMessage", is("Transfer amount must be greater than 0"))
                .body("code", is(303));
    }

    @Test
    public void testCreateTransferAccountToIsNotSetUpFailed() {
        transferDto.setAccountTo(null);
        given().contentType("application/json")
                .body(transferDto)
                .when().post("/transfer")
                .then().statusCode(409)
                .body("developerMessage", is("Field accountTo must be set up"))
                .body("code", is(305));
    }

    @Test
    public void testCreateTransferAccountFromBalanceAmountIsNotEnoughFailed() {
        transferDto.setBalance(new BigDecimal(1000.6));
        given().contentType("application/json")
                .body(transferDto)
                .when().post("/transfer")
                .then().statusCode(409)
                .body("developerMessage", is(String.format("AccountDto %s hasn't enough money for transfer operation", idFrom)))
                .body("code", is(307));
    }

}

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

    private AccountDto accountDtoFrom;

    private AccountDto accountDtoTo;

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
        TransferDto transferDto = new TransferDto();
        transferDto.setBalance(new BigDecimal(100));
        transferDto.setAccountFrom(idFrom);
        transferDto.setAccountTo(idTo);

        given().contentType("application/json")
                .body(transferDto)
                .when().post("/transfer")
                .then().statusCode(201)
                .body("accountFrom", equalTo(idFrom))
                .body("accountTo", equalTo(idTo))
                .body("balance", equalTo(transferDto.getBalance().intValue()))
                .body("id", isA(String.class))
                .body("created", notNullValue());

        BigDecimal balanceFrom =new BigDecimal( given().contentType("application/json").pathParam("id",UUID.fromString(idFrom))
                .when()
                .get("/account/{id}").then().statusCode(200).extract().jsonPath().getString("balance"));

        BigDecimal balanceTo =new BigDecimal( given().contentType("application/json").pathParam("id",UUID.fromString(idTo))
                .when()
                .get("/account/{id}").then().statusCode(200).extract().jsonPath().getString("balance"));

        assertThat(balanceFrom,closeTo(accountDtoFrom.getBalance().subtract(transferDto.getBalance()),new BigDecimal(0.00001)));
        assertThat(balanceTo,closeTo(accountDtoTo.getBalance().add(transferDto.getBalance()),new BigDecimal(0.00001)));


    }

    @Test
    public void testCreateTransferDuplicateAccount() {
        TransferDto transferDto = new TransferDto();
        transferDto.setBalance(new BigDecimal(100));
        transferDto.setAccountFrom(idFrom);
        transferDto.setAccountTo(idFrom);

        given().contentType("application/json")
                .body(transferDto)
                .when().post("/transfer")
                .then().statusCode(409)
                .body("developerMessage", is("Account from and accountTo must be different"))
                .body("code", is(304));
    }

    @Test
    public void testCreateTransferBalanceIsNotSetUp() {
        TransferDto transferDto = new TransferDto();
        transferDto.setAccountFrom(idFrom);
        transferDto.setAccountTo(idFrom);

        given().contentType("application/json")
                .body(transferDto)
                .when().post("/transfer")
                .then().statusCode(409)
                .body("developerMessage", is("Transfer amount in transfer isn't set up"))
                .body("code", is(301));
    }

    @Test
    public void testCreateTransferAccountFromIsNotSetUp() {
        TransferDto transferDto = new TransferDto();
        transferDto.setAccountTo(idFrom);
        transferDto.setBalance(new BigDecimal(100));
        given().contentType("application/json")
                .body(transferDto)
                .when().post("/transfer")
                .then().statusCode(409)
                .body("developerMessage", is("Field accountFrom must be set up"))
                .body("code", is(302));
    }

    @Test
    public void testCreateTransferBalanceIsNegative() {
        TransferDto transferDto = new TransferDto();
        transferDto.setAccountFrom(idFrom);
        transferDto.setAccountTo(idFrom);
        transferDto.setBalance(new BigDecimal(-100));
        given().contentType("application/json")
                .body(transferDto)
                .when().post("/transfer")
                .then().statusCode(409)
                .body("developerMessage", is("Transfer amount must be greater than 0"))
                .body("code", is(303));
    }

    @Test
    public void testCreateTransferAccountToIsNotSetUp() {
        TransferDto transferDto = new TransferDto();
        transferDto.setAccountFrom(idFrom);
        transferDto.setBalance(new BigDecimal(100));
        given().contentType("application/json")
                .body(transferDto)
                .when().post("/transfer")
                .then().statusCode(409)
                .body("developerMessage", is("Field accountTo must be set up"))
                .body("code", is(305));
    }

    @Test
    public void testCreateTransferAccountFromBalanceAmountIsNotEnough() {
        TransferDto transferDto = new TransferDto();
        transferDto.setAccountFrom(idFrom);
        transferDto.setAccountTo(idTo);
        transferDto.setBalance(new BigDecimal(1000.6));
        given().contentType("application/json")
                .body(transferDto)
                .when().post("/transfer")
                .then().statusCode(409)
                .body("developerMessage", is(String.format("AccountDto %s hasn't enough money for transfer operation",idFrom)))
                .body("code", is(307));
    }
}

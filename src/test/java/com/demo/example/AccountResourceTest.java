package com.demo.example;

import com.demo.example.dto.AccountDto;
import org.exparity.hamcrest.date.DateMatchers;
import org.glassfish.jersey.server.ResourceConfig;
import org.hamcrest.Matchers;
import org.junit.Test;

import javax.ws.rs.core.Application;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.notNullValue;

public class AccountResourceTest extends BaseTest {

    AccountDto accountDto;

    @Override
    protected Application configure() {
        return new ResourceConfig(AccountResource.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        accountDto = new AccountDto();
        accountDto.setAccountName("testAccount");
        accountDto.setBalance(new BigDecimal(123.4));
        accountDto.setEmail("aaa@google.com");
    }

    @Test
    public void testCreateAccount() {
        given().contentType("application/json")
                .body(accountDto)
                .when().post("/account")
                .then().statusCode(201)
                .body("accountName", equalTo("testAccount"))
                .body("id", notNullValue())
                .body("id", isA(String.class))
                .body("created", notNullValue());
    }
}

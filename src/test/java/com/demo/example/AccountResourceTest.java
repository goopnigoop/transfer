package com.demo.example;

import com.demo.example.dto.AccountDto;
import com.demo.example.exception.AppExceptionMapper;
import com.jayway.restassured.http.ContentType;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Test;

import javax.ws.rs.core.Application;
import java.math.BigDecimal;
import java.util.UUID;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AccountResourceTest extends BaseTest {

    private AccountDto accountDto;

    private AccountDto accountDtoSecond;

    private String id;

    @Override
    protected Application configure() {
        return new ResourceConfig(AccountResource.class,AppExceptionMapper.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        accountDto = new AccountDto();
        accountDto.setAccountName("testAccount");
        accountDto.setBalance(new BigDecimal(123.4));
        accountDto.setEmail("aaa@google.com");
        id = given().contentType("application/json")
                .body(accountDto).post("/account").then().statusCode(201).extract().jsonPath().getString("id");
        accountDtoSecond = new AccountDto();
        accountDtoSecond.setAccountName("second");
        accountDtoSecond.setEmail("fff@ttt.com");
        accountDtoSecond.setBalance(new BigDecimal(123.5));
    }

    @Override
    public void tearDown() throws Exception {
        try {
            given().contentType(ContentType.JSON)
                    .pathParam("id", UUID.fromString(id))
                    .delete("/account/{id}")
                    .then()
                    .statusCode(204);
        } finally {
            super.tearDown();
        }

    }

    @Test
    public void testCreateAccount() {
        given().contentType("application/json")
                .body(accountDtoSecond)
                .when().post("/account")
                .then().statusCode(201)
                .body("accountName", equalTo("second"))
                .body("id", notNullValue())
                .body("id", isA(String.class))
                .body("created", notNullValue());
    }

    @Test
    public void testCreateAccountWrongEmail() {
        AccountDto accountDto = new AccountDto();
        accountDto.setEmail("wrong");
        accountDto.setAccountName("account");
        given().contentType("application/json")
                .body(accountDto)
                .when().post("/account")
                .then().statusCode(400);
    }

    @Test
    public void testGetAccount() {
        given().contentType(ContentType.JSON)
                .queryParam("email", "aaa@google.com")
                .queryParam("accountName", "testAccount")
                .when()
                .get("/account")
                .then()
                .statusCode(200)
                .body("get(0).accountName",equalTo(accountDto.getAccountName()));
    }

    @Test
    public void testGetAccountByWrongId() {
        given().contentType(ContentType.JSON)
                .pathParam("id",UUID.randomUUID())
                .when()
                .get("/account/{id}")
                .then()
                .statusCode(404)
                .body("message",equalTo("Entry is not found"));
    }

    @Test
    public void testGetAccountById() {
        given().contentType(ContentType.JSON)
                .pathParam("id",UUID.fromString(id))
                .when()
                .get("/account/{id}")
                .then()
                .statusCode(200)
                .body("accountName",equalTo(accountDto.getAccountName()));
    }

    @Test
    public void testGetAccountWrongData() {
        given().contentType(ContentType.JSON)
                .queryParam("email", "aaa@google.com")
                .queryParam("accountName", "wrong")
                .when()
                .get("/account")
                .then()
                .statusCode(200)
                .body(is("[]"));
    }

    @Test
    public void testUpdateAccount() {
        accountDto.setBalance(new BigDecimal(10000));
        accountDto.setAccountName("new");
        accountDto.setEmail("new@email.com");
        given().contentType(ContentType.JSON)
                .pathParam("id",UUID.fromString(id))
                .body(accountDto)
                .put("/account/{id}")
                .then()
                .statusCode(204).and();

        given().contentType(ContentType.JSON)
                .pathParam("id",UUID.fromString(id))
                .get("/account/{id}")
                .then()
                .statusCode(200)
                .body("email",is("new@email.com"))
                .body("accountName",is("new"));
    }

    @Test
    public void testDeleteAccount() {
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountName("delete");
        accountDto.setEmail("delete@ya.com");
        accountDto.setBalance(new BigDecimal(100));

        String id = given().contentType("application/json")
                .body(accountDto).post("/account").then().statusCode(201).extract().jsonPath().getString("id");

        given().contentType(ContentType.JSON)
                .pathParam("id",UUID.fromString(id))
                .delete("/account/{id}")
                .then()
                .statusCode(204);

        given().contentType(ContentType.JSON)
                .pathParam("id",UUID.fromString(id))
                .when()
                .get("/account/{id}")
                .then()
                .statusCode(404)
                .body("message",equalTo("Entry is not found"));

    }

}

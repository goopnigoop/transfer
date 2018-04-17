package com.demo.example;

import com.jayway.restassured.RestAssured;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.BeforeClass;

public class BaseTest extends JerseyTest {

    private static final int TEST_PORT = 7003;
    private static final String TEST_URL = "http://localhost";

    static {
        System.setProperty("jersey.config.test.container.port", String.valueOf(TEST_PORT));
    }



    @BeforeClass
    public static void configureRestAssured()
    {
        RestAssured.port = TEST_PORT;
        RestAssured.baseURI = TEST_URL;
    }


}

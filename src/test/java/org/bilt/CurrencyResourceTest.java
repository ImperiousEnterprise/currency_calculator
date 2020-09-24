package org.bilt;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.bilt.models.Currency;
import org.bilt.models.CurrencyPair;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
//@TestHTTPEndpoint(CurrencyResource.class)
public class CurrencyResourceTest {
    @Test
    public void testAddCurrencyEndpoint() {
        CurrencyPair c = new CurrencyPair();
        c.base = "euro";
        c.quote = "usd";
        c.rate = 1.25;
        given().contentType(ContentType.JSON).body(c).when().post("/currency")
                .then()
                .statusCode(201)
                .body(is("Have fun and try some conversions"));
    }
    @Test
    public void testAddCurrencyEndpointwithError() {
        CurrencyPair c = new CurrencyPair();
        c.base = "euro";
        c.rate = 1.25;
        given().contentType(ContentType.JSON).body(c).when().post("/currency")
                .then()
                .statusCode(400);
    }
    @Test
    public void testGetAllCurrencyEndpoint() {
        CurrencyPair c = new CurrencyPair();
        c.base = "euro";
        c.quote = "usd";
        c.rate = 1.25;
        given().contentType(ContentType.JSON).body(c).when().post("/currency")
                .then()
                .statusCode(201)
                .body(is("Have fun and try some conversions"));


        given().get("/currency")
                .then().statusCode(200).body("size()",is(2));
    }

    @Test
    public void testGetSpecificCurrencyEndpoint() {
        CurrencyPair c = new CurrencyPair();
        c.base = "euro";
        c.quote = "usd";
        c.rate = 1.25;
        given().contentType(ContentType.JSON).body(c).when().post("/currency")
                .then()
                .statusCode(201)
                .body(is("Have fun and try some conversions"));

        given().get("/currency/euro")
                .then().statusCode(200).body(containsString("USD"));
    }
    @Test
    public void testGetNoneExistentCurrencyEndpoint() {
        given().get("/currency/ohio")
                .then().statusCode(200).body(notNullValue());
    }
    @Test
    public void testCurrencyConversionEndpoint() {
        CurrencyPair c = new CurrencyPair();
        c.base = "euro";
        c.quote = "usd";
        c.rate = 1.25;
        given().contentType(ContentType.JSON).body(c).when().post("/currency")
                .then()
                .statusCode(201)
                .body(is("Have fun and try some conversions"));

        Currency cur = new Currency();
        cur.from = "euro";
        cur.to = "usd";
        cur.amount = 1;
        given().contentType(ContentType.JSON).body(cur).when().post("/currency/convert")
                .then()
                .statusCode(200)
                .body("result",is(1.25f));
    }
    @Test
    public void testNonExistantCurrencyConversionEndpoint() {

        Currency cur = new Currency();
        cur.from = "yen";
        cur.to = "usd";
        cur.amount = 1;
        given().contentType(ContentType.JSON).body(cur).when().post("/currency/convert")
                .then()
                .statusCode(400)
                .body(is("One of your currencies does not exist"));
    }
}

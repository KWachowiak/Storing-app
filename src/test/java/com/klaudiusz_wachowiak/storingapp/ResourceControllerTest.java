package com.klaudiusz_wachowiak.storingapp;

import com.klaudiusz_wachowiak.storingapp.dto.ResourceInDto;
import com.klaudiusz_wachowiak.storingapp.dto.ResourceOutDto;
import org.junit.Before;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class ResourceControllerTest {

    @Before
    public void dbRecordsInit() {
        ResourceInDto resourceInDto1 = new ResourceInDto();
        resourceInDto1.setName("html1");
        resourceInDto1.setUrl("http://bing.com/");
        resourceInDto1.setDescription("");
        resourceInDto1.setFileName("");

        ResourceInDto resourceInDto2 = new ResourceInDto();
        resourceInDto2.setName("image1");
        resourceInDto2.setUrl("https://imagazine.pl/wp-content/uploads/2014/12/javaprob-001.png");
        resourceInDto2.setDescription("");
        resourceInDto2.setFileName("");

        ResourceInDto resourceInDto3 = new ResourceInDto();
        resourceInDto3.setName("pdf1");
        resourceInDto3.setUrl("https://www.iso.org/files/live/sites/isoorg/files/archive/pdf/en/annual_report_2009.pdf");
        resourceInDto3.setDescription("");
        resourceInDto3.setFileName("");

        given()
                .port(8080)
                .contentType("application/json")
                .body(resourceInDto1)
                .when()
                .post("/resources");
        given()
                .port(8080)
                .contentType("application/json")
                .body(resourceInDto2)
                .when()
                .post("/resources");
        given()
                .port(8080)
                .contentType("application/json")
                .body(resourceInDto3)
                .when()
                .post("/resources");
    }

    @Test
    public void getAllResources_200() {

        assertEquals(
                3,
                given().port(8080)
                       .when()
                       .get("/resources")
                       .then()
                       .statusCode(200)
                       .extract().body().as(ResourceOutDto[].class).length
        );
    }

    @Test
    public void getByContentNameOrUrl_404() {
        given().port(8080)
               .when()
               .get("/resources?like=xml")
               .then()
               .statusCode(404);
    }

    @Test
    public void getByContentNameOrUrl_200NamesMatched() {

        assertEquals(
                "html1",
                given()
                        .port(8080)
                        .when()
                        .get("/resources?like=html")
                        .then()
                        .statusCode(200)
                        .extract().body().as(ResourceOutDto[].class)[0].getName());
    }

    @Test
    public void addNewContentToDB_400CauseIsABadFormat() {
        ResourceInDto sourceInDto = new ResourceInDto();
        sourceInDto.setName("test5");
        sourceInDto.setUrl("http://home.agh.edu.pl/~giergiel/JPO/Java_01.pdf");
        sourceInDto.setDescription("");

        given()
                .port(8080)
                .contentType("application/json")
                .body(sourceInDto)
                .when()
                .post("/resources")
                .then()
                .statusCode(400);
    }

    @Test
    public void addNewContentToDB_409CauseIsAlreadyInDb() {
        ResourceInDto resourceInDto = new ResourceInDto();
        resourceInDto.setName("html1");
        resourceInDto.setUrl("http://bing.com/");
        resourceInDto.setFileName("html");
        resourceInDto.setDescription("");


        given()
                .port(8080)
                .contentType("application/json")
                .body(resourceInDto)
                .when()
                .post("/resources")
                .then()
                .statusCode(409);
    }

    @Test
    public void addNewSourceToDB_200AndNameMatched() {
        ResourceInDto resourceInDto = new ResourceInDto();
        resourceInDto.setName("test4");
        resourceInDto.setUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Fronalpstock_big.jpg/1200px-Fronalpstock_big.jpg");
        resourceInDto.setFileName("");
        resourceInDto.setDescription("");

        assertEquals(
                "test4",
                given()
                        .port(8080)
                        .contentType("application/json")
                        .body(resourceInDto)
                        .when()
                        .post("/resources")
                        .then()
                        .statusCode(200)
                        .extract().body().as(ResourceOutDto.class).getName()
        );
    }

    @Test
    public void downloadMultiSourceWhenNameIsGiven_404CauseNoSuchNameInDb() {
        given()
                .port(8080)
                .when()
                .get("/resources/download/test")
                .then()
                .statusCode(404);
    }

    @Test
    public void downloadMultiSourceWhenNameIsGiven_200() {
        given()
                .port(8080)
                .when()
                .get("/resources/download/image1")
                .then()
                .statusCode(200);
    }

    @Test
    public void downloadMultiSourceWhenNameIsGiven_200AndLength2() {
        assertEquals(
                2,
                given()
                        .port(8080)
                        .when()
                        .get("/resources/download/many/image1,pdf1")
                        .then()
                        .statusCode(200)
                        .extract().body().as(ResourceOutDto[].class).length
        );
    }

}

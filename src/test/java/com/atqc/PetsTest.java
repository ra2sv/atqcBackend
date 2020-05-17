package com.atqc;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import javafx.animation.PathTransitionBuilder;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class PetsTest extends RestAPIBaseTest {

    long petId;

    @Test (priority = 1)
    @Description("Negative test: send request without body")
    public void negativeCreatePet(){
        given()
                .contentType("application/json")
                .baseUri("https://petstore.swagger.io/v2")
                .header("Access-Token", "some_token")
                .body("")
                .when()
                .post("/pet")
                .then()
                .statusCode(405)
                .body("code", is(405))
                .body("type", notNullValue())
                .body("message", is("no data"));
    }

    @Test (priority = 2)
    @Description("Positive test: create new pet")
    public void positiveCreatePet(){

        petId = given()
                .contentType("application/json")
                .baseUri("https://petstore.swagger.io/v2")
                .header("Access-Token", "some_token")
                .body("{\n" +
                        "  \"id\": 0,\n" +
                        "  \"category\": {\n" +
                        "    \"id\": 0,\n" +
                        "    \"name\": \"Dogs\"\n" +
                        "  },\n" +
                        "  \"name\": \"Rudolf\",\n" +
                        "  \"photoUrls\": [],\n" +
                        "  \"tags\": [\n" +
                        "    {\n" +
                        "      \"id\": 0,\n" +
                        "      \"name\": \"Big\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"status\": \"available\"\n" +
                        "}")
        .when()
                .post("/pet")
        .then()
                .statusCode(200)
                .body("category.name", is("Dogs"))
                .body("name", is("Rudolf"))
                .body("tags[0].name", is("Big"))
                .body("status", is("available"))
                .extract().path("id");
        System.out.println("Pet id was fount: " + petId);
    }

    @Test (priority = 3, dataProvider = "InvalidIdErrors")
    @Description("Negative test: Get pet by fake id")
    public void negativeGetPetById(int fakeId, int code, String message){
        given()
                .contentType("application/json")
                .baseUri("https://petstore.swagger.io/v2")
                .header("Access-Token", "some_token")
        .when()
                .get("/pet/{petId}", fakeId)
        .then()
                .statusCode(code)
                .body("type", notNullValue())
                .body("message", is(message));
    }

    @DataProvider(name = "InvalidIdErrors")
    private Object[][] fakeId() {

        return new Object[][] {
                {0, 404, "Pet not found"},
                {-55, 404, "Pet not found"},
        };
    }

    @Test (priority = 4)
    @Description("Positive test: Get just created pet")
    public void positiveGetPetById(){
        given()
                .contentType("application/json")
                .baseUri("https://petstore.swagger.io/v2")
                .header("Access-Token", "some_token")
        .when()
                .get("/pet/{petId}", petId)
        .then()
                .statusCode(200)
                .body("category.name", is("Dogs"))
                .body("name", is("Rudolf"))
                .body("tags[0].name", is("Big"))
                .body("status", is("available"))
                .body("id", is(petId));
    }

    @Test (priority = 5, dependsOnMethods = "positiveCreatePet")
    @Description("Positive test: Delete pet by Id")
    public void positiveDeletePetById(){
        given()
                .contentType("application/json")
                .baseUri("https://petstore.swagger.io/v2")
                .header("Access-Token", "some_token")
        .when()
                .delete("/pet/{petId}", petId)
        .then()
                .statusCode(200)
                .body("code", is(200))
                .body("type", notNullValue())
                .body("message", is(Long.toString(petId)));

        //Make sure that pet was deleted
        given()
                .contentType("application/json")
                .baseUri("https://petstore.swagger.io/v2")
                .header("Access-Token", "some_token")
        .when()
                .get("/pet/{petId}", petId)
        .then()
                .statusCode(404)
                .body("type", notNullValue())
                .body("message", is("Pet not found"));
    }
}

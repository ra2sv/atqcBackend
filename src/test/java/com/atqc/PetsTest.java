package com.atqc;

import com.atqc.models.PetModel;
import io.qameta.allure.Description;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import com.github.javafaker.Faker;

import java.util.Arrays;
import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;


public class PetsTest extends RestAPIBaseTest {

    int petId;
    String petName;
    PetModel validBody;
    PetModel updatedPet;

    @Test (priority = 1)
    @Description("Negative test: send request without body")
    public void negativeCreatePet(){

        given()
                .spec(REQUEST_SPEC)
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
    @Description("Negative test: send request without Required fields - Name")
    public void negativeWithoutName(){

        PetModel body =
                PetModel.builder()
                        .id(12)
                        .category(PetModel.PetCategoryModel.selectCategory(PetModel.PetCategoryModel.CategoryName.BIG))
                        .photoUrls(Collections.singletonList("http://google.com"))
                        .tags(Arrays.asList(PetModel.PetTagModel.selectTag(PetModel.PetTagModel.TagName.SMALL)))
                        .status(PetModel.PetStatus.AVAILABLE.toString().toLowerCase())
                        .build();

        given()
                .spec(REQUEST_SPEC)
                .header("Access-Token", "some_token")
                .body(body)
                .when()
                .post("/pet")
                .then()
                .statusCode(405)
                .body("code", is(405))
                .body("type", notNullValue())
                .body("message", is("no data"));
    }

    @Test (priority = 3)
    @Description("Negative test: send request without Required fields - Name")
    public void negativeWithoutPhotos(){

        PetModel body =
                PetModel.builder()
                        .id(12)
                        .category(PetModel.PetCategoryModel.selectCategory(PetModel.PetCategoryModel.CategoryName.BIG))
                        .tags(Arrays.asList(PetModel.PetTagModel.selectTag(PetModel.PetTagModel.TagName.SMALL)))
                        .status(PetModel.PetStatus.AVAILABLE.toString().toLowerCase())
                        .build();

        given()
                .spec(REQUEST_SPEC)
                .header("Access-Token", "some_token")
                .body(body)
                .when()
                .post("/pet")
                .then()
                .statusCode(405)
                .body("code", is(405))
                .body("type", notNullValue())
                .body("message", is("no data"));
    }

    @Test (priority = 4)
    @Description("Positive test: create new pet")
    public void positiveCreatePet(){

        validBody = PetModel.random();
        PetModel petModel = given()
                .spec(REQUEST_SPEC)
                .header("Access-Token", "some_token")
                .body(validBody)
                .when()
                .post("/pet")
                .then()
                .statusCode(200)
                .extract().as(PetModel.class);

        assertEquals(petModel, validBody);
        System.out.println("Pet id: " + petModel.getId() + " Pep Name: " + petModel.getName() + "was created");
        petId = petModel.getId();
        petName = petModel.getName();
    }

    @Test (priority = 5)
    @Description("Positive test: Get just created pet")
    public void positiveGetPetById(){
        PetModel petModel = given()
                .spec(REQUEST_SPEC)
                .header("Access-Token", "some_token")
                .when()
                .get("/pet/{petId}", petId)
                .then()
                .statusCode(200)
                .extract().as(PetModel.class);
        assertEquals(petModel, validBody);
    }

    @Test (priority = 6)
    @Description("Positive test: Update just created pet")
    public void positivePetUpdate(){

        Faker faker = new Faker();
        updatedPet = PetModel.builder()
                .id(petId)
                .category(PetModel.PetCategoryModel.selectCategory(PetModel.PetCategoryModel.CategoryName.BIG))
                .name(faker.name().name())
                .photoUrls(Collections.singletonList("http://google.com"))
                .tags(Arrays.asList(PetModel.PetTagModel.selectTag(PetModel.PetTagModel.TagName.SMALL)))
                .status(PetModel.PetStatus.AVAILABLE.toString().toLowerCase())
                .build();

        PetModel petModel = given()
                .spec(REQUEST_SPEC)
                .header("Access-Token", "some_token")
                .body(updatedPet)
                .when()
                .put("/pet")
                .then()
                .statusCode(200)
                .extract().as(PetModel.class);

        assertEquals(petModel, updatedPet);
        System.out.println("Pet id: " + petModel.getId() + " Pet Name: " + petModel.getName() + "was created");
        petName = petModel.getName();
    }

    @Test (priority = 7, enabled=false)
    @Description("Positive test: Update pet by id")
    public void positivePetUpdateById(){

        Faker faker = new Faker();
        String newName = faker.name().name();
        String newStatus = PetModel.PetStatus.PENDING.toString().toLowerCase();

        given()
                .spec(REQUEST_SPEC)
                .header("Access-Token", "some_token")
                .formParam("name", newName)
                .formParam("status", newStatus)
                .when()
                .post("/pet/{petId}", 56068)
                .then()
                .statusCode(200)
                //Check update info
                .body("id", is(petId))
                .body("name", is(newName))
                .body("tags",is(newStatus));

        //System.out.println("Pet id: " + petModel.getId() + " Pet Name: " + petModel.getName() + "was updated");
        //petName = petModel.getName();
    }

    @Test (priority = 8, dependsOnMethods = "positiveCreatePet")
    @Description("Positive test: Delete pet by Id")
    public void positiveDeletePetById(){
        given()
                .spec(REQUEST_SPEC)
                .header("Access-Token", "some_token")
                .when()
                .delete("/pet/{petId}", petId)
                .then()
                .statusCode(200)
                .body("code", is(200))
                .body("type", notNullValue())
                .body("message", is(Integer.toString(petId)));

        //Make sure that pet was deleted
        negativeGetPetById(petId, 404, "Pet not found");
    }

    @Test (priority = 8, dataProvider = "InvalidIdErrors")
    @Description("Negative test: Get pet by fake id")
    public void negativeGetPetById(int fakeId, int code, String message){
        given()
                .spec(REQUEST_SPEC)
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

}
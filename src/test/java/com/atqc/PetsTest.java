package com.atqc;

import com.atqc.models.PetCategoryModel;
import com.atqc.models.PetModel;
import com.atqc.models.UserModel;
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
    @Description("Negative test: send request without Required fields")
    public void negativeMissingRequiredFields(){

        given()
                .spec(REQUEST_SPEC)
                .header("Access-Token", "some_token")
                .body("{" +
                        "  'id': 0,\n" +
                        "  'category': {\n" +
                        "    'id': 0,\n" +
                        "    'name': 'string'\n" +
                        "  },\n" +
                        "  'tags': [\n" +
                        "    {\n" +
                        "      'id': 0,\n" +
                        "      'name': 'string'\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  'status': 'available'\n" +
                        "}")
                .when()
                .post("/pet")
                .then()
                .statusCode(405)
                .body("code", is(405))
                .body("type", notNullValue())
                .body("message", is("no data"));
    }

    @Test (priority = 3)
    @Description("Positive test: create new pet")
    public void positiveCreatePet(){

        PetModel createDog = PetModel.createDog();
        PetModel petModel = given()
                .spec(REQUEST_SPEC)
                .header("Access-Token", "some_token")
                .body(createDog)
        .when()
                .post("/pet")
        .then()
                .statusCode(200)
                .body("category.id", is(1))
                .body("category.name", is("Dogs"))
                //.body("name", is("Rudolf"))
                .body("photoUrls[0]", notNullValue())
                .body("tags[0].id", is(3))
                .body("tags[0].name", is("Big"))
                .body("status", is("available"))
                //.extract().path("id");
                .extract().as(PetModel.class);
        System.out.println("Pet id was fount: " + petModel.getId());
    }

    @Test (priority = 4, dataProvider = "InvalidIdErrors")
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

    @Test (priority = 5)
    @Description("Positive test: Get just created pet")
    public void positiveGetPetById(){
        given()
                .spec(REQUEST_SPEC)
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

    @Test (priority = 6, dependsOnMethods = "positiveCreatePet")
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
                .body("message", is(Long.toString(petId)));

        //Make sure that pet was deleted
        negativeGetPetById(Math.toIntExact(petId), 404, "Pet not found");
    }

}

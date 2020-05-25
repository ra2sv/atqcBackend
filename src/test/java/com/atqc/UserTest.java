package com.atqc;

import com.atqc.models.UserModel;
import io.qameta.allure.Description;
import lombok.extern.log4j.Log4j;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Log4j
public class UserTest extends RestAPIBaseTest {

    UserModel user = UserModel.create();

    @Test
    @Description("Create User with valid data")
    public void positivePostNewUser() {

        given()
                .spec(REQUEST_SPEC)
                .header("Access-Token", "dfhdh=validtoken=gfsdfdfh")
                .body(user)
        .when()
                .post("/user")
        .then()
                .statusCode(200)
                .body("code", is(200))
                .body("type", is("unknown"))
                .body("message", notNullValue());

    }

    @Test
    @Description("Get User by valid username")
    public void positiveGetUserByUsername() {

        String userName = "rosario.kub";

        UserModel userModel = given()
                .spec(REQUEST_SPEC)
                .header("Access-Token", "dfhdh=validtoken=gfsdfdfh")
        .when()
                .get("/user/{username}", userName)
        .then()
                .statusCode(200)
                .body("username", equalTo(userName))
                .body("firstName", is(not(emptyString())))
                .extract().as(UserModel.class);

        System.out.println(userModel.getLastName());

    }

    @Test(dataProvider = "responseErrors")
    @Description("Get user by invalid username")
    public void negativeGetUserByInvalidUsername(String username, int code, String message) {
        given()
                .spec(REQUEST_SPEC)
                .header("Access-Token", "dfhdh=validtoken=gfsdfdfh")
        .when()
                .get("/user/{username}", username)
        .then()
                .statusCode(code)
                .body("code", is(1))
                .body("type", is("error"))
                .body("message", equalTo(message));

    }

    @DataProvider(name = "responseErrors")
    private Object[][] provider() {

        return new Object[][] {

                {"rdfhdh", 404, "User not found"},
                {"!2$%*", 404, "User not found"}

        };
    }



}

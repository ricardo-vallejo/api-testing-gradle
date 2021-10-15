import data.factory.RegisterUserDataFactory;
import model.RegisterUserRequest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TestsWithDataFactory extends BaseTest{

    @Test
    public void registerValidUser(){
        RegisterUserRequest validUser = RegisterUserDataFactory.validDefaultUser();

        given()
                .body(validUser)
                .when()
                .post("/register")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("id", equalTo(4));
    }

    @Test
    public void registerUserDiffEmail(){
        RegisterUserRequest validUser = RegisterUserDataFactory.validUserDiffEmail();

        given()
                .body(validUser)
                .when()
                .post("/register")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("error", equalTo("Note: Only defined users succeed registration"));
    }

    @Test
    public void registerUserDiffPassword(){
        RegisterUserRequest validUser = RegisterUserDataFactory.validUserDiffPass();

        given()
                .body(validUser)
                .when()
                .post("/register")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("id", equalTo(4));
    }

    @Test
    public void registerUserNullInfo(){
        RegisterUserRequest validUser = RegisterUserDataFactory.nullInformation();

        given()
                .body(validUser)
                .when()
                .post("/register")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("error", equalTo("Missing email or username"));
    }

    @Test
    public void registerUserMissingInformation(){
        RegisterUserRequest validUser = RegisterUserDataFactory.missingAllInformation();

        given()
                .body(validUser)
                .when()
                .post("/register")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("error", equalTo("Missing email or username"));
    }
}

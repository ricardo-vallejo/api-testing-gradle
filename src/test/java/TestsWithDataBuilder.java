import data.builder.RegisterUserDataBuilder;
import model.RegisterUserRequest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TestsWithDataBuilder extends BaseTest{

    @Test
    public void registerValidUser(){
        RegisterUserRequest validUser = RegisterUserDataBuilder.registerUserDataBuilder().build();

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
        RegisterUserRequest validUser = RegisterUserDataBuilder
                .registerUserDataBuilder()
                .withWrongEmail()
                .build();

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
        RegisterUserRequest validUser = RegisterUserDataBuilder
                .registerUserDataBuilder()
                .withPassword("qweasfsdfgas")
                .build();

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
        RegisterUserRequest validUser = RegisterUserDataBuilder
                .registerUserDataBuilder()
                .withEmail(null)
                .withPassword(null)
                .build();

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
        RegisterUserRequest validUser = RegisterUserDataBuilder
                .registerUserDataBuilder()
                .withEmptyEmail()
                .withEmptyPassword()
                .build();

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

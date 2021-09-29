import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ReqRestTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://reqres.in"; // It works to specify the base URI for all the test in the class.
        RestAssured.basePath = "/api"; // It works to specify the base PATH for all the test in the class.
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter()); // It works to log all used in a request or/and response.
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
    }

    @Test
    public void loginTest() {
        String endPoint = "https://reqres.in/api/login";
        String body = "{\n" +
                "    \"email\": \"eve.holt@reqres.in\",\n" +
                "    \"password\": \"cityslicka\"\n" +
                "}";

        String response = given().
                log().all().  // It works to show all the parameters used to send the request.
                contentType(ContentType.JSON). //
                body(body).
                when().post(endPoint).
                then().
                log().all(). // It works to show all the parameters send in the response.
                extract().asString(); // It works to extract the response into a String type.

        System.out.println(response);
    }

    @Test
    public void loginTestHamcrestMatchers() {
        String endPoint = "https://reqres.in/api/login";
        String body = "{\n" +
                "    \"email\": \"eve.holt@reqres.in\",\n" +
                "    \"password\": \"cityslicka\"\n" +
                "}";

        var response = given().
                contentType(ContentType.JSON).
                body(body).when().post(endPoint).then();

        response.statusCode(200).body("token", notNullValue()); // Normal assertion.
        response.assertThat().statusCode(200).and().body("token", notNullValue()); // Syntactic sugar assertion.
    }

    @Test
    public void validateId() {
        String endPoint = "https://reqres.in/api/users/2";
        var response =
                given().log().all().
                when().get(endPoint).
                then().log().all();

        response.assertThat().
                statusCode(200).and().
                body("data.id", equalTo(2));
    }

    @Test
    public void getUsersWithParams() {
        String endPoint = "https://reqres.in/api/users";
        var response =
                given().log().all().queryParam("page", 2).
                        when().get(endPoint).
                        then().log().all();

        response.assertThat().
                statusCode(200).and().
                body("data.id[0]", equalTo(7)).and().
                body("page", equalTo(2));
    }

    @Test
    public void getUserWithId() {
        String endPoint = "/users/2";
        var response =
                given().
                when().get(endPoint).
                then();

        response.assertThat().
                statusCode(HttpStatus.SC_OK).and(). // We can use constant enums instead of hardcode values.
                body("data.id", equalTo(2));
    }

    @Test
    public void deleteUser() {
        String endPoint = "/users/2";
        var response =
                given().
                        when().delete(endPoint).
                        then();

        response.assertThat().
                statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    public void patchMethod() {
        String endPoint = "/users/2";
        String body = "{\n" +
                "    \"name\": \"Morpheus\"" +
                "}";

        String expectedName = given().body(body).
                when().patch(endPoint).
                then().
                statusCode(HttpStatus.SC_OK).
                extract().jsonPath().getString("name"); // It works to extract a specific field of a JSON structure.

        assertThat(expectedName, equalTo("Morpheus"));
    }

    @Test
    public void putMethod() {
        String endPoint = "/users/2";
        String body = "{\n" +
                "    \"name\": \"morpheus\",\n" +
                "    \"job\": \"zion president\"\n" +
                "}";

        String expectedName = given().body(body).
                when().put(endPoint).
                then().
                statusCode(HttpStatus.SC_OK).
                extract().jsonPath().getString("job");

        assertThat(expectedName, equalTo("zion president"));
    }

    @Test
    public void extractResponseObjects() {
        String endPoint = "/users";

        Response response = given().queryParam("page", 2).when().get(endPoint);

        Headers headers = response.getHeaders();
        int statusCode = response.getStatusCode();
        String body = response.getBody().asString();
        String contentType = response.getContentType();

        assertThat(statusCode, equalTo(HttpStatus.SC_OK));

        System.out.println("----------");
        System.out.println("Body: " + body);
        System.out.println("Headers: " + headers.toString());
        System.out.println("----------");
        System.out.println("Content-Type: " + contentType);
        System.out.println("----------");
        System.out.println(headers.get("Connection"));
        System.out.println(headers.get("Date"));
        System.out.println(response.getHeader("Cache-Control"));


    }

}

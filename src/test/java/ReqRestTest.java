import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import model.RegisterUserRequest;
import model.RegisterUserResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

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

        // This map the response in a rest-assured response type
        Response response = given().queryParam("page", 2).when().get(endPoint);

        // You can get several response elements
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
        System.out.println("----------");
        System.out.println(headers.get("Cache-Control")); // This return the key and value.
        System.out.println(response.getHeader("Cache-Control")); // This return only the value.
    }

    @Test
    public void extractJsonResponseObjects() {
        String endPoint = "/users";

        String response = given().queryParam("page", 2)
                .when().get(endPoint)
                .then().extract().asString();

        // from() method is used to read the object and extract information.
        int page = from(response).getInt("page");
        int totalPages = from(response).getInt("total_pages");
        int idFirstUser = from(response).getInt("data[0].id"); // Both ways work to extract a position
        int idSecondUser = from(response).getInt("data.id[1]");

        System.out.println("Page: " + page);
        System.out.println("Total Pages: " + totalPages);
        System.out.println("Id First User: " + idFirstUser);
        System.out.println("Id Second User: " + idSecondUser);

        List<Map> usersWithIdGreaterThan10 = from(response).get("data.findAll { user -> user.id > 10}");
        String email = usersWithIdGreaterThan10.get(0).get("email").toString();

        System.out.println("Email: " + email);

        usersWithIdGreaterThan10.forEach(System.out::println);

        String lastName = "Edwards";
        /*
        * from() or with() both used to catch the JSON response.
        * param() used to pass parameters to the lambda expression.
        * get() used to write the lambda expression.*/

        List<Map> user = from(response).param("lastName", lastName).get("data.findAll { user -> user.id > 9 && user.last_name == lastName}");
        int userId = (int) user.get(0).get("id");

        System.out.println("User Id: " + userId);
    }

    @Test
    public void createUser() {
        String endPoint = "/users";
        String body = "{\n" +
                "    \"name\": \"morpheus\",\n" +
                "    \"job\": \"leader\"\n" +
                "}";

        String response = given()
                .body(body)
                .when()
                .post(endPoint)
                .then()
                .extract().body().asString();

        User user = from(response).getObject("", User.class);
        System.out.println("User Id: " + user.getId());
        System.out.println("User Name: " + user.getName());
    }

    @Test
    public void registerUser() {
        String endPoint = "/register";

        // We create a serialized request using a java object.
        RegisterUserRequest userRequest = new RegisterUserRequest("eve.holt@reqres.in", "pistol");

        // We receive a deserialized response using a java object.
        RegisterUserResponse userResponse = given()
                .body(userRequest)
                .when()
                .post(endPoint)
                .as(RegisterUserResponse.class);

        // We use assertThat() to create assertions.
        assertThat(userResponse.getId(), equalTo(4));
        assertThat(userResponse.getToken(), equalTo("QpwL5tke4Pnpja7X4"));

    }

}

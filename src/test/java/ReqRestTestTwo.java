import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ReqRestTestTwo extends BaseTest{

    @Test
    public void getSingleUserTest(){
        given()
                .when()
                .get("users/2")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("data.id", equalTo(2));
    }
}

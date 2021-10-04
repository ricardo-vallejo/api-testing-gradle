import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseTest {

    private static final Logger LOGGER = LogManager.getLogger();

    @BeforeAll
    public static void setup() throws IOException {
        LOGGER.info("Staring Configuration.");
        RestAssured.requestSpecification = defaultRequestSpecification();
        LOGGER.info("Finishing Configuration.");
    }

    private static RequestSpecification defaultRequestSpecification() throws IOException {
        List<Filter> filters = new ArrayList<>();
        filters.add(new RequestLoggingFilter());
        filters.add(new ResponseLoggingFilter());

        return new RequestSpecBuilder().setBaseUri(ConfVariables.getHost())
                .setBasePath(ConfVariables.getPath())
                .addFilters(filters)
                .setContentType(ContentType.JSON).build();
    }

    public ResponseSpecification defaultResponseSpecification() {
        return new ResponseSpecBuilder()
                .expectStatusCode(HttpStatus.SC_OK)
                .expectContentType(ContentType.JSON)
                .build();
    }
}

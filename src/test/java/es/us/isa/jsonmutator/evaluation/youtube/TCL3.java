package es.us.isa.jsonmutator.evaluation.youtube;

import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TCL3 {

    private static final String OAI_JSON_URL = "src/test/resources/youtube_oas.yaml";
    private final OpenApiValidationFilter validationFilter = new OpenApiValidationFilter(OAI_JSON_URL);

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://content.googleapis.com/youtube/v3";
    }

    @Test
    public void searchNoParamsTest() {
        try {
            System.out.println("\n\n------------- REQUEST --------------\n");
            Response response = RestAssured
                    .given()
                    .log().all()
//                    .param("key", "AIzaSyA3f9mWXs3Gbm7MSwBU4hUD2D7CJKJq9hk")
//                    .param("part", "snippet")
//                    .filter(validationFilter)
                    .when()
                    .get("/search");

            System.out.println("\n\n------------- RESPONSE -------------\n");
            response.then().log().all();
            response.then().statusCode(400);
            System.out.println("Test passed.");
        } catch (RuntimeException ex) {
            System.err.println(ex.getMessage());
            fail(ex.getMessage());
        }
    }
}

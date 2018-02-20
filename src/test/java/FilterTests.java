import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;
import utils.api.Authorization;
import utils.api.JiraApiActions;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class FilterTests {

    private String filterJSON = "{" + "\"name\": \"Filter\"," +
            "\"description\": \"Filter for test\"," +
            "\"jql\": \"project = QAAUT AND issuetype = Story AND status = Backlog AND priority = Medium\"," +
            "\"favourite\": true" +
            "}";


    @Test(groups = {"CRITICAL", "HTTP"})
    public void authentication() {
        Authorization.loginToJIRA();
    }

    @Test
    public void createFilter() {
        RestAssured.baseURI = "http://jira.hillel.it:8080";
        ValidatableResponse response;
        response = given().
                header("Accept", "application/json").
                header("Content-Type", "application/json").
                header("Cookie", "JSESSIONID=" + Authorization.JSESSIONID).
                body(filterJSON).
                when().
                post("/rest/api/2/filter").
                then().log().all().
                statusCode(200).contentType(ContentType.JSON);

        String responseBody = response.extract().asString();
        System.out.print("\nRESPONSE: " + responseBody);
        String filterId = response.extract().path("id").toString();

        /* confirm create */
        given().
                header("Accept", "application/json").
                header("Content-Type", "application/json").
                header("Cookie", "JSESSIONID=" + Authorization.JSESSIONID).
                body(filterJSON).
                when().
                get("/rest/api/2/filter/" + filterId).
                then().log().all().
                statusCode(200).contentType(ContentType.JSON);

        /* delete filter */
        given().
                header("Content-Type", "application/json").
                header("Cookie", "JSESSIONID=" + Authorization.JSESSIONID).
                when().
                delete("/rest/api/2/filter/" + filterId).
                then().log().all().
                statusCode(204).contentType(ContentType.JSON);

        /* confirm delete */
        given().
                header("Content-Type", "application/json").
                header("Cookie", "JSESSIONID=" + Authorization.JSESSIONID).
                when().
                get("/rest/api/2/filter/" + filterId).
                then().log().all().
                statusCode(400).contentType(ContentType.JSON);

    }
}
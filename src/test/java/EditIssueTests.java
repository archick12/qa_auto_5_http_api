import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class EditIssueTests {
    String sessionId = "";
    String projectId = "10502";
    String issueType = "13561";

    @BeforeTest(groups = {"CRITICAL","HTTP"})
    public void authentication(){
        RestAssured.baseURI = "http://jira.hillel.it:8080";
        String credentialsJSON = "{" +
                "\"username\": \"qa_auto_5_team_1\"," +
                "\"password\": \"qa_auto_5_team_1\"" +
                "} "
                ;

        /* HTTP Requests for login*/
        sessionId = given().
            header("Content-Type", "application/json").
            body(credentialsJSON).
            when().
            post("/rest/auth/1/session").
            then().
            extract().
            path("session.value");

        System.out.printf("\nSESSION: "+ sessionId);
}


    @Test(groups = {"Regression, HTTP"},dependsOnGroups = {"CRITICAL","HTTP"})
    public void commentCRUD(){
        RestAssured.baseURI = "http://jira.hillel.it:8080";
        ValidatableResponse response;

         /* test data and parameters */

        String jsonForAddComment = "JSON for your test";
        String jsonForUpdateComment = "";

        // TO DO your test

    }

    @Test(groups = {"Regression, HTTP"},dependsOnGroups = {"CRITICAL","HTTP"})
    public void descriptionCRUD(){
        RestAssured.baseURI = "http://jira.hillel.it:8080";
        ValidatableResponse response;

         /* test data and parameters */

        String jsonForAddDescription = "{" + "\" body\": \"My description\""+"}";
        response = given().
                header("Content-Type", "application/json").
                header("Cookie", "JSESSIONID=" + sessionId).
                body(jsonForAddDescription).
                when().
                post("/rest/api/2/issue/13561").
                then().log().all().
                statusCode(201).contentType(ContentType.JSON);

        String responseBody = response.extract().asString();
        System.out.printf("\nRESPONSE: " + responseBody);
        String issueKey = response.extract().path("key");


        /* delete issue */
        String jsonForDeleteDescription = "{" + "\" body\": \"\""+"}";
        given().
                header("Content-Type", "application/json").
                header("Cookie", "JSESSIONID=" + sessionId).
                body(jsonForDeleteDescription).
                when().
                delete("/rest/api/2/issue/13561" + issueKey).
                then().
                statusCode(204).contentType(ContentType.JSON);
    }
    }




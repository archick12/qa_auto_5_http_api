import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class RestAPIIssueTests {
    String sessionId = "";
    String projectId = "10502";
    String issueType = "13561";

    @BeforeTest(groups = {"CRITICAL", "HTTP"})
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


    @Test(groups = {"Regression, HTTP"},dependsOnGroups = {"CRITICAL", "HTTP"})
    public void commentCRUD(){
        RestAssured.baseURI = "http://jira.hillel.it:8080";
        ValidatableResponse response;

         /* test data and parameters */

        String jsonForAddComment = "JSON for your test";
        String jsonForUpdateComment = "";

        // TO DO your test

    }

    @Test(groups = {"Regression, HTTP"},dependsOnGroups = {"CRITICAL", "HTTP"})
    public void descriptionCRUD(){
        RestAssured.baseURI = "http://jira.hillel.it:8080";
        ValidatableResponse response;

         /* test data and parameters */

        String jsonForAddDescription = "JSON for your test";


        // TO DO your test
    }

    @Test(groups = {"Regression, HTTP"},dependsOnGroups = {"CRITICAL", "HTTP"})
    public void remoteIssueLinksCRUD() {
        RestAssured.baseURI = "http://jira.hillel.it:8080";
        ValidatableResponse response;
        String jsonForAddRemoteLink = "{\"object\": {\n" +
                "\"url\": \"https://obmenka.od.ua/\",\n" +
                "\"title\": \"obmenka\"\n" +
                "}}";

        /* HTTP Request to get ID's for Remote Issue Links*/
        String result = given().
                header("Accept","application/json").
                header("Cookie", "JSESSIONID=" + sessionId).
                when().
                get("/rest/api/2/issue/13561/remotelink").
                then().
                log().all().
                extract().
                asString();

        /* create new link to Remote Issue */
        response = given().
                header("Accept","application/json").
                header("Content-Type","application/json").
                header("Cookie", "JSESSIONID=" + sessionId).
                body(jsonForAddRemoteLink).
                when().
                post("/rest/api/2/issue/13561/remotelink").
                then().log().all().
                statusCode(201).contentType(ContentType.JSON);
        String responseBody = response.extract().asString();
        System.out.printf("\nRESPONSE: " + responseBody);
        String linkId = response.extract().path("id").toString();
        
        /* delete link to Remote Issue */
        given().
                header("Content-Type", "application/json").
                header("Cookie", "JSESSIONID=" + sessionId).
                body(jsonForAddRemoteLink).
                when().
                delete("/rest/api/2/issue/" + issueType +"/remotelink/"+ linkId).
                then().log().all().
                statusCode(204).contentType(ContentType.JSON);

    }
}

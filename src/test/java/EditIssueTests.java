import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class EditIssueTests {
    String projectId = "10502";
    String issueType = "13306";

    @Test
    public void commentCRUD(){
        RestAssured.baseURI = "http://jira.hillel.it:8080";
        ValidatableResponse response;

         /* test data and parameters */



        String credentialsJSON = "{" +
                "\"username\": \"qa_auto_5_team_1\"," +
                "\"password\": \"qa_auto_5_team_1\"" +
                "} ";

        String issueJSON = "JSON for your test";

         /* HTTP Requests for login*/
        String sessionId = given().
                header("Content-Type", "application/json").
                body(credentialsJSON).
                when().
                post("/rest/auth/1/session").
                then().
                extract().
                path("session.value");

        System.out.printf("\nSESSION: " + sessionId);

        // TO DO your test

    }

    @Test
    public void remoteIssueLinkCRUD(){
        RestAssured.baseURI = "http://jira.hillel.it:8080";
        ValidatableResponse response;

         /* test data and parameters */


        String credentialsJSON = "{" +
                "\"username\": \"qa_auto_5_team_1\"," +
                "\"password\": \"qa_auto_5_team_1\"" +
                "} ";


        String issueJSON = "JSON for your test";

         /* HTTP Requests for login*/
        String sessionId = given().
                header("Content-Type", "application/json").
                body(credentialsJSON).
                when().
                post("/rest/auth/1/session").
                then().
                extract().
                path("session.value");

        System.out.printf("\nSESSION: " + sessionId);


        // TO DO your test
    }

    @Test
    public void worklogCRUD(){
        RestAssured.baseURI = "http://jira.hillel.it:8080";
        ValidatableResponse response;

         /* test data and parameters */


        String credentialsJSON = "{" +
                "\"username\": \"qa_auto_5_team_1\"," +
                "\"password\": \"qa_auto_5_team_1\"" +
                "} ";


        String issueJSON = "JSON for your test";

         /* HTTP Requests for login*/
        String sessionId = given().
                header("Content-Type", "application/json").
                body(credentialsJSON).
                when().
                post("/rest/auth/1/session").
                then().
                extract().
                path("session.value");

        System.out.printf("\nSESSION: " + sessionId);

        // TO DO your test

    }

    @Test
    public void assignIssue(){
        RestAssured.baseURI = "http://jira.hillel.it:8080";
        ValidatableResponse response;

         /* test data and parameters */


        String credentialsJSON = "{" +
                "\"username\": \"qa_auto_5_team_1\"," +
                "\"password\": \"qa_auto_5_team_1\"" +
                "} ";

        String issueJSON = "JSON for your test";

         /* HTTP Requests for login*/
        String sessionId = given().
                header("Content-Type", "application/json").
                body(credentialsJSON).
                when().
                post("/rest/auth/1/session").
                then().
                extract().
                path("session.value");

        System.out.printf("\nSESSION: " + sessionId);

        // TO DO your test


    }

    @Test
    public void addWatcherToIssue(){
        RestAssured.baseURI = "http://jira.hillel.it:8080";
        ValidatableResponse response;

         /* test data and parameters */


        String credentialsJSON = "{" +
                "\"username\": \"qa_auto_5_team_1\"," +
                "\"password\": \"qa_auto_5_team_1\"" +
                "} ";

        String issueJSON = "JSON for your test";

         /* HTTP Requests for login*/
        String sessionId = given().
                header("Content-Type", "application/json").
                body(credentialsJSON).
                when().
                post("/rest/auth/1/session").
                then().
                extract().
                path("session.value");

        System.out.printf("\nSESSION: " + sessionId);

        // TO DO your test

    }
}

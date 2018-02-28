import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class IssueRawTest {

  String projectId = "10508";
  String issueType = "10107";

  @Test
  public void createIssueRawTest() {
    RestAssured.baseURI = "http://jira.hillel.it:8080"; // rest assured preconfiguration
    ValidatableResponse response;

    /* test data and parameters */
    String summary = "Test Summary";
    String assignee = "Artur Piluck";

    String credentialsJSON = "{" +
            "\"username\": \"qa_auto_5_team_1\"," +
            "\"password\": \"qa_auto_5_team_1\"" +
            "} ";

    String issueJSON = "{\"fields\":{" +
            "\"summary\":\"" + summary + "\"," +
            "\"issuetype\":{\"id\":\"" + issueType + "\"}," +
            "\"project\":{\"id\":\"" + projectId + "\"}," +
//                "\"reporter\":{\"name\":\"" + assignee + "\"}," +
            "\"assignee\":{\"name\":\"" + assignee + "\"}" +
            "}}";

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

    /* HTTP Request to get ID's for project and issue type*/
    String result = given().
            header("Content-Type", "application/json").
            header("Cookie", "JSESSIONID=" + sessionId).
            when().
            get("/rest/api/2/issue/createmeta").
            then().
            extract().
            asString();

    /* create issue */
    response = given().
            header("Content-Type", "application/json").
            header("Cookie", "JSESSIONID=" + sessionId).
            body(issueJSON).
            when().
            post("/rest/api/2/issue").
            then().log().all().
            statusCode(201).contentType(ContentType.JSON);

    String responseBody = response.extract().asString();
    System.out.printf("\nRESPONSE: " + responseBody);
    String issueKey = response.extract().path("key");

    /* delete issue */
    given().
            header("Content-Type", "application/json").
            header("Cookie", "JSESSIONID=" + sessionId).
            body(issueJSON).
            when().
            delete("/rest/api/2/issue/" + issueKey).
            then().
            statusCode(204).contentType(ContentType.JSON);
  }
}
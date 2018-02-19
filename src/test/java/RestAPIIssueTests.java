import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;
import utils.api.Authorization;
import utils.api.JiraApiActions;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class RestAPIIssueTests {

  String issueId = "13561";

  @Test(groups = {"CRITICAL", "HTTP"})
  public void authentication() {
    Authorization.loginToJIRA();
  }

  @Test(groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
  public void commentCRUD() {
    /* HTTP Request for Create Comment*/
    String comment = "test";
    ValidatableResponse response = JiraApiActions.createComment(issueId, comment);
    String myCommentFromServer = response.extract().path("body");
    String commentId = response.extract().path("id").toString();
    assertEquals(comment, myCommentFromServer);

    /* HTTP Request for Update Comment*/
    String newComment = "New Comment";
    response = JiraApiActions.updateComment(issueId, commentId, newComment);
    String newCommentFromServer = response.extract().path("body");
    assertEquals(newComment, newCommentFromServer);
        
    /* HTTP Request for Delete Comment*/
    JiraApiActions.deleteComment(issueId, commentId);
    String responseBody = response.extract().asString();

    /* HTTP Request for confirm that comment was deleted*/
    JiraApiActions.getNonExistingComment(issueId, commentId);
  }

  @Test(groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
  public void descriptionCRUD() {
    RestAssured.baseURI = "http://jira.hillel.it:8080";
    ValidatableResponse response;
    String description = "My description";
    String jsonForAddDescription = "{\"fields\":{" + "\"description\": \"My description\"" + "}}";

          /* HTTP Request for add description to issue*/
    response = given().
        header("Content-Type", "application/json").
        header("Cookie", "JSESSIONID=" + Authorization.JSESSIONID).
        body(jsonForAddDescription).
        when().
        put("/rest/api/2/issue/13561").
        then().log().all();

    String responseBody = response.extract().asString();
    String jsonForDeleteDescription = "{\"fields\":{" + "\"description\": \"\"" + "}}";

        /* HTTP Request for delete description from issue*/
    response = given().
        header("Content-Type", "application/json").
        header("Cookie", "JSESSIONID=" + Authorization.JSESSIONID).
        body(jsonForDeleteDescription).
        when().
        put("/rest/api/2/issue/13561").
        then().log().all().
        statusCode(204).contentType(ContentType.JSON);

  }

  @Test(groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
  public void remoteIssueLinksCRUD() {
    RestAssured.baseURI = "http://jira.hillel.it:8080";
    ValidatableResponse response;
    String jsonForAddRemoteLink = "{\"object\": {\n" +
        "\"url\": \"https://obmenka.od.ua/\",\n" +
        "\"title\": \"obmenka\"\n" +
        "}}";
    String result = given().
        header("Accept", "application/json").
        header("Cookie", "JSESSIONID=" + Authorization.JSESSIONID).
        when().
        get("/rest/api/2/issue/13561/remotelink").
        then().
        log().all().
        extract().
        asString();

        /* create new link to Remote Issue */
    response = given().
        header("Accept", "application/json").
        header("Content-Type", "application/json").
        header("Cookie", "JSESSIONID=" + Authorization.JSESSIONID).
        body(jsonForAddRemoteLink).
        when().
        post("/rest/api/2/issue/13561/remotelink").
        then().log().all().
        statusCode(201).contentType(ContentType.JSON);
    String responseBody = response.extract().asString();
    String linkId = response.extract().path("id").toString();

        /* delete link to Remote Issue */
    given().
        header("Content-Type", "application/json").
        header("Cookie", "JSESSIONID=" + Authorization.JSESSIONID).
        body(jsonForAddRemoteLink).
        when().
        delete("/rest/api/2/issue/" + issueId + "/remotelink/" + linkId).
        then().log().all().
        statusCode(204).contentType(ContentType.JSON);

  }
}

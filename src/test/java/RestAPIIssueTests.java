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

    String description = "My description";
    ValidatableResponse response = JiraApiActions.createDescription(issueId, description);
    String myDescriptionFromServer = response.extract().path("body");
    String descriptionId = response.extract().path("id").toString();
    assertEquals(description,myDescriptionFromServer);

    /* HTTP Request for add description to issue*/
    JiraApiActions.deleteDescription(issueId, descriptionId);
    String responseBody = response.extract().asString();

    /* HTTP Request for delete description from issue*/
    JiraApiActions.getNonExistingDescription(issueId, descriptionId);
  }

  @Test(groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
  public void remoteIssueLinksCRUD() {

    ValidatableResponse response = JiraApiActions.addRemoteLink(issueId);
    String link = "https://obmenka.od.ua";
    String myLinkFromServer = response.extract().path("body");
    String linkId = response.extract().path("id").toString();
    assertEquals(link, myLinkFromServer);

    /* HTTP Request for Delete Remote Link*/
    JiraApiActions.deleteRemoteLinkIssue(issueId, linkId);
    String responseLinkBody = response.extract().asString();

    /* HTTP Request for confirm that Remote Link was deleted*/
    JiraApiActions.getNonExistingRemoteLink(issueId, linkId);

  }
}

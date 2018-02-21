import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;
import utils.api.Authorization;
import utils.api.JiraApiActions;

import java.util.ArrayList;

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
    /* HTTP Request for add description to issue*/
    String description = "My description";
    
    ValidatableResponse response = JiraApiActions.createDescription(issueId, description);
    String descriptionFromServer = response.extract().path("body");
    //String descriptionId =response.extract().path("id").toString();
    assertEquals(description, descriptionFromServer);

    /* HTTP Request for delete description from issue*/
    String emptyDescription = "";
    JiraApiActions.createDescription(issueId, emptyDescription);
    String editedDescriptionFromServer = response.extract().path("body");
    assertEquals(emptyDescription, editedDescriptionFromServer);
    }

  @Test(groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
  public void addRemoteLinkToIssue() {
    /* HTTP Request for create new link to Remote Issue */
    String title = "obmenka";
    ValidatableResponse response = JiraApiActions.addRemoteLink(issueId);
    String remoteLinkFromServer = response.extract().path("object.url");// перепроверить path
    String remoteLinkId = response.extract().path("id");
    assertEquals(title, remoteLinkFromServer);

    /* HTTP Request for delete link to Remote Issue */
    //JiraApiActions.deleteRemoteLinkIssue(issueId, remoteLinkId );

    /* HTTP Request for confirm that remoteLink was deleted*/
    //JiraApiActions.getNonExistingRemoteLink(issueId, remoteLinkId);
  }
  }

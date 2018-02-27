import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;
import utils.api.Authorization;
import utils.api.JiraAnnotation;
import utils.api.JiraApiActions;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class RestAPIIssueTests {

  String issueId = "QAAUT-487";

  @JiraAnnotation(id = "QAAUT-17", comment = "test")
  @Test(groups = {"CRITICAL", "HTTP"})
  public void authentication() {
    // TODO do not remove or test will fail with NonExistingGroupException
    Authorization.loginToJIRA();
  }
  @JiraAnnotation(id = "QAAUT-17", comment = "test")
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

  @JiraAnnotation(id = "QAAUT-17", comment = "test")
  @Test(groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
  public void descriptionCRUD() {
    /* HTTP Request for add description to issue*/
    String description = "My description";

    JiraApiActions.createDescription(issueId, description);
    ValidatableResponse response = JiraApiActions.getDescription(issueId);
    String descriptionFromServer = response.extract().path("fields.description");
    assertEquals(description, descriptionFromServer);

    /* HTTP Request for delete description from issue*/

    String emptyDescription = "";
    JiraApiActions.createDescription(issueId, emptyDescription);
    response = JiraApiActions.getDescription(issueId);
    descriptionFromServer = response.extract().path("fields.description");
    assertEquals(null, descriptionFromServer);
  }

  @JiraAnnotation(id = "QAAUT-17", comment = "test")
  @Test(groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
  public void RemoteLinkToIssueCRUD() {
    /* HTTP Request for create new link to Remote Issue */
    String url = "http://jira.hillel.it:8080/browse/QAAUT-289";
    String title = "github";

    ValidatableResponse response = JiraApiActions.addRemoteLink(issueId, url, title);
    String urlOfRemoteLinkFromServer = response.extract().path("self").toString();
    String linkedIssueId = response.extract().path("id").toString();

    // https://stackoverflow.com/questions/11007008/whats-the-best-way-to-check-if-a-string-contains-a-url-in-java-android
    boolean containsOnlyNumbers = linkedIssueId.matches("[0-9]+") && linkedIssueId.length() > 1;
    boolean containsUrlToLink = urlOfRemoteLinkFromServer.contains("/remotelink/" + linkedIssueId);

    assertEquals(true, containsOnlyNumbers);
    assertEquals(true, containsUrlToLink);

    /* HTTP Request for delete link to Remote Issue */
    JiraApiActions.deleteRemoteLinkIssue(issueId, linkedIssueId);

    /* HTTP Request for confirm that remoteLink was deleted*/
    JiraApiActions.getNonExistingRemoteLink(issueId, linkedIssueId);
  }
}

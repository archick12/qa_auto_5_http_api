package utils.api;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.testng.Assert;
import utils.data.JiraJsonObjectHelper;

/**
 * Wrapper class for all possible actions in JIRA API
 */
public class JiraApiActions {

  public static ValidatableResponse createIssue(String issueJSON) {
    ValidatableResponse response = HTTPMethods.post(APIPathes.issue, issueJSON);
    Assert.assertEquals(response.extract().statusCode(), 201);
    Assert.assertTrue(response.extract().contentType().contains(ContentType.JSON.toString()));
    return response.extract().path("key");
  }

  public static ValidatableResponse getIssue(String issueKey) {
    ValidatableResponse response = HTTPMethods.get(APIPathes.issue + issueKey);
    Assert.assertEquals(response.extract().statusCode(), 200);
    Assert.assertTrue(response.extract().contentType().contains(ContentType.JSON.toString()));
    return response;
  }

  public static ValidatableResponse getNonExistingIssue(String issueKey) {
    ValidatableResponse response = HTTPMethods.get(APIPathes.issue + issueKey);
    Assert.assertEquals(response.extract().statusCode(), 404);
    Assert.assertTrue(response.extract().contentType().contains(ContentType.JSON.toString()));
    return response;
  }

  public static void deleteIssue(String issueKey) {
    ValidatableResponse response = HTTPMethods.delete(APIPathes.issue + issueKey);
    Assert.assertEquals(response.extract().statusCode(), 204);
    Assert.assertTrue(response.extract().contentType().contains(ContentType.JSON.toString()));
  }

  public static ValidatableResponse createComment(String issueId, String comment) {
    String jsonForAddComment = JiraJsonObjectHelper.generateJSONForComment(comment);
    ValidatableResponse response = HTTPMethods
        .post(String.format(APIPathes.commentInIssue, issueId), jsonForAddComment);
    response.statusCode(201);
    response.contentType(ContentType.JSON);
    return response;
  }

  public static ValidatableResponse updateComment(String issueId, String commentId,
      String comment) {
    String jsonForComment = JiraJsonObjectHelper.generateJSONForComment(comment);
    ValidatableResponse response = HTTPMethods
        .put(String.format(APIPathes.existingCommentInIssue, issueId, commentId), jsonForComment);

    response.statusCode(200);
    response.contentType(ContentType.JSON);
    return response;
  }

  public static ValidatableResponse deleteComment(String issueId, String commentId) {
    ValidatableResponse response = HTTPMethods
        .delete(String.format(APIPathes.existingCommentInIssue, issueId, commentId));

    response.statusCode(204);
    response.contentType(ContentType.JSON);
    return response;
  }

  public static void getNonExistingComment(String issueId, String commentId) {
    ValidatableResponse response = HTTPMethods
        .get(String.format(APIPathes.existingCommentInIssue, issueId, commentId));
    response.statusCode(404);
    response.contentType(ContentType.JSON);
  }

  public static ValidatableResponse createDescription(String issueId, String description) {
    String jsonForAddDescription = JiraJsonObjectHelper.generateJSONForDescription(description);
    ValidatableResponse response = HTTPMethods
            .put(String.format(APIPathes.descriptionInIssue, issueId), jsonForAddDescription);
    response.log().all();
    response.statusCode(204);
    response.contentType(ContentType.JSON);
    return response;
  }

  /*public static ValidatableResponse deleteDescription(String issueId, String descriptionId) {
    ValidatableResponse response = HTTPMethods
            .delete(String.format(APIPathes.descriptionInIssue, issueId, descriptionId));

    response.statusCode(204);
    response.contentType(ContentType.JSON);
    return response;
  }

  public static void getNonExistingDescription(String issueId, String descriptionId) {
    ValidatableResponse response = HTTPMethods
            .get(String.format(APIPathes.descriptionInIssue, issueId, descriptionId));
    response.statusCode(404);
    response.contentType(ContentType.JSON);
  }*/

  public static ValidatableResponse addRemoteLink(String url) {
    String jsonForAddRemoteLink = JiraJsonObjectHelper.generateJSONForRemoteLink(url);
    ValidatableResponse response = HTTPMethods
            .post(String.format(APIPathes.remoteIssueLink, url), jsonForAddRemoteLink);
    response.statusCode(200);
    response.contentType(ContentType.JSON);
    return response;
  }

  public static ValidatableResponse deleteRemoteLinkIssue(String issueId, String linkedIssueId) {
    ValidatableResponse response = HTTPMethods
            .delete(String.format(APIPathes.existingRemoteIssueLink, issueId, linkedIssueId));

    response.statusCode(204);
    response.contentType(ContentType.JSON);
    return response;
  }
  public static void getNonExistingRemoteLink(String issueId, String linkedIssueId) {
    ValidatableResponse response = HTTPMethods
            .get(String.format(APIPathes.existingRemoteIssueLink, issueId, linkedIssueId));
    response.statusCode(404);
    response.contentType(ContentType.JSON);
  }
}

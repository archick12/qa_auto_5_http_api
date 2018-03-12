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
//    return response.extract().path("key");
      return response;
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

  public static ValidatableResponse searchForIssues(String jqlString) {
    String jsonForSearch = JiraJsonObjectHelper.generateJSONForSearch(jqlString);
    ValidatableResponse response = HTTPMethods
            .post(APIPathes.searchIssues, jsonForSearch);
    response.statusCode(200);
    response.contentType(ContentType.JSON);
    return response;
    }

  public static ValidatableResponse createDescription(String issueId, String description) {
    String jsonForAddDescription = JiraJsonObjectHelper.generateJSONForDescription(description);
    ValidatableResponse response = HTTPMethods
            .put(String.format(APIPathes.descriptionInIssue, issueId), jsonForAddDescription);
    response.statusCode(204);
    response.contentType(ContentType.JSON);
    return response;
  }

  public static ValidatableResponse getDescription(String issueId) {
    ValidatableResponse response = HTTPMethods
              .get(String.format(APIPathes.descriptionInIssue, issueId));
    response.statusCode(200);
    response.contentType(ContentType.JSON);
    return response;
  }

  public static ValidatableResponse addRemoteLink(String issueId, String url, String title) {
    String jsonForAddRemoteLink = JiraJsonObjectHelper.generateJSONForRemoteLink(url, title);
    ValidatableResponse response = HTTPMethods
            .post(String.format(APIPathes.remoteIssueLink, issueId), jsonForAddRemoteLink);
    response.statusCode(201);
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

  public static ValidatableResponse createFilter(String requestBody) {
    ValidatableResponse response = HTTPMethods.post(APIPathes.filter, requestBody);
    Assert.assertEquals(response.extract().statusCode(), 200);
    Assert.assertTrue(response.extract().contentType().contains(ContentType.JSON.toString()));
    return response.log().body();
  }

  public static ValidatableResponse getFilter(String filterId) {
    ValidatableResponse response = HTTPMethods.get(APIPathes.filter + filterId);
    Assert.assertEquals(response.extract().statusCode(), 200);
    Assert.assertTrue(response.extract().contentType().contains(ContentType.JSON.toString()));
    return response.log().body();
  }

  public static ValidatableResponse deleteFilter(String filterId) {
    ValidatableResponse response = HTTPMethods.delete(String.format(APIPathes.existingFilter, filterId));
    Assert.assertEquals(response.extract().statusCode(), 204);
    Assert.assertTrue(response.extract().contentType().contains(ContentType.JSON.toString()));
    return response.log().body();
  }

  public static ValidatableResponse getDeletedFilter(String filterId) {
    ValidatableResponse response = HTTPMethods.delete(String.format(APIPathes.existingFilter, filterId));
    Assert.assertEquals(response.extract().statusCode(), 400);
    Assert.assertTrue(response.extract().contentType().contains(ContentType.JSON.toString()));
    return response.log().body();
  }

  public static ValidatableResponse updateFilter(String filterID, String requestBody) {
    ValidatableResponse response = HTTPMethods.put(APIPathes.filter + filterID, requestBody);
    Assert.assertEquals(response.extract().statusCode(), 200);
    Assert.assertTrue(response.extract().contentType().contains(ContentType.JSON.toString()));
    return response.log().body();
  }
//
  public static ValidatableResponse setFavouriteFlag(String filterID, String requestBody) {
    ValidatableResponse response = HTTPMethods.put(String.format(APIPathes.filterFavourite, filterID), requestBody);
    Assert.assertEquals(response.extract().statusCode(), 200);
    Assert.assertTrue(response.extract().contentType().contains(ContentType.JSON.toString()));
    return response.log().body();
  }

  public static ValidatableResponse deleteFavouriteFlag(String filterID) {
    ValidatableResponse response = HTTPMethods.delete(String.format(APIPathes.filterFavourite, filterID));
    Assert.assertEquals(response.extract().statusCode(), 200);
    Assert.assertTrue(response.extract().contentType().contains(ContentType.JSON.toString()));
    return response.log().body();
  }

  public static ValidatableResponse getFilterPermission(String filterID, String permissionID) {
    ValidatableResponse response = HTTPMethods.get(String.format(APIPathes.filterPermission, filterID, permissionID));
    Assert.assertEquals(response.extract().statusCode(), 200);
    Assert.assertTrue(response.extract().contentType().contains(ContentType.JSON.toString()));
    return response.log().body();
  }

  public static ValidatableResponse getAllFilterPermissions(String filterID) {
    ValidatableResponse response = HTTPMethods.get(String.format(APIPathes.filterAllPermissions, filterID));
    Assert.assertEquals(response.extract().statusCode(), 200);
    Assert.assertTrue(response.extract().contentType().contains(ContentType.JSON.toString()));
    return response.log().body();
  }

  public static ValidatableResponse addFilterPermission(String filterID, String requestBody) {
    ValidatableResponse response = HTTPMethods.post(String.format(APIPathes.filterAllPermissions, filterID), requestBody);
    Assert.assertEquals(response.extract().statusCode(), 201);
    Assert.assertTrue(response.extract().contentType().contains(ContentType.JSON.toString()));
    return response.log().body();
  }

  public static ValidatableResponse deleteFilterPermission(String filterID, String permissionID) {
    ValidatableResponse response = HTTPMethods.delete(String.format(APIPathes.filterPermission, filterID, permissionID));
    Assert.assertEquals(response.extract().statusCode(), 204);
    Assert.assertTrue(response.extract().contentType().contains(ContentType.JSON.toString()));
    return response.log().body();
  }

  public static ValidatableResponse getIssueTransitions(String issueID) {
    ValidatableResponse response = HTTPMethods.get(String.format(APIPathes.issueTransitions, issueID));
    Assert.assertEquals(response.extract().statusCode(), 200);
    Assert.assertTrue(response.extract().contentType().contains(ContentType.JSON.toString()));
    return response.log().body();
  }

  public static ValidatableResponse doIssueTransition(String issueID, String requestBody) {
    ValidatableResponse response = HTTPMethods.post(String.format(APIPathes.issueTransitions, issueID), requestBody);
    Assert.assertEquals(response.extract().statusCode(), 204);
    Assert.assertTrue(response.extract().contentType().contains(ContentType.JSON.toString()));
    return response.log().body();
  }

}

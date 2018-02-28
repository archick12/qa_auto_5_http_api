import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import utils.api.Authorization;
import utils.api.JiraApiActions;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static utils.api.JiraApiActions.*;
import static utils.data.JiraJsonObjectHelper.generateJSONForIssue;
import static utils.data.JiraJsonObjectHelper.generateJSONForNewFilter;

public class IssueRawTest{

  private final String projectId = "10508";
  private final String issueType = "10107";
  /* test data and parameters */
  private String summary = "Test Summary";
  private String assignee = "Artur Piluck";

  @Test(groups = {"CRITICAL", "HTTP"})
  public void authentication() {
    Authorization.loginToJIRA();
  }
  @Test(groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
  public void createIssueRawTest() {
    String jsonIssue = generateJSONForIssue(projectId, issueType, summary, assignee);
    String createIssueId = createIssue(jsonIssue).extract().path("id").toString();
    getIssue(createIssueId);
    deleteIssue(createIssueId);
    getNonExistingIssue(createIssueId);
  }

}
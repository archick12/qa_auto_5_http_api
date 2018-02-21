import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;
import utils.api.Authorization;
import utils.api.JiraApiActions;

import static org.testng.Assert.assertEquals;

public class RestAPISearchTests {

  @Test(groups = {"CRITICAL", "HTTP"})
  public void authentication() {
    Authorization.loginToJIRA();
  }

  @Test(groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
  public void searchByProject() {
    /* HTTP Request for search for issues by project*/
    String jqlString = "project = QAAUTO5";
    ValidatableResponse response = JiraApiActions.searchForIssues(jqlString);
    response.log().all();
// TODO wip
    String projectName = response.extract().path("project", "name");
//    String commentId = response.extract().path("id").toString();
//    assertEquals(comment, myCommentFromServer);


  }


  @Test(groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
  public void searchByAssignee() {
    /* HTTP Request for search for issues by Assignee*/
    String jqlString = Authorization.username;
    ValidatableResponse response = JiraApiActions.searchForIssues(jqlString);
    response.log().all();

  }

}

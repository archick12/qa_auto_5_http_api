import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;
import utils.api.Authorization;
import utils.api.JiraApiActions;

import java.util.List;
import static org.testng.Assert.assertEquals;

public class RestAPISearchTests {
  static final Logger logger = Logger.getLogger(RestAPISearchTests.class);


  @Test(groups = {"CRITICAL", "HTTP"})
  public void authentication() {
    Authorization.loginToJIRA();
  }

  @Test(groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
  public void searchByProject() {
    /* HTTP Request for search for issuse by project*/
    String projectName = "QAAuto5";
    ValidatableResponse response = JiraApiActions.searchForIssues("project = "+ projectName);
    response.log().all();
    List<String> searchResultIssuesProjectName = response.extract().jsonPath().getList("issues.fields.project.name");
    for (String item : searchResultIssuesProjectName) {
      assertEquals(true, item.contains(projectName));
//      assertEquals(projectName, item);
//      TODO fix logger
      logger.info("Assertion passed: " + item);
      System.out.println("Assertion passed: " + item);
    }


  }


  @Test(groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
  public void searchByAssignee() {
    /* HTTP Request for search for issuse by Assignee*/
    String assignee = Authorization.username;
    ValidatableResponse response = JiraApiActions.searchForIssues("assignee = " + assignee);
    response.log().all();

  }

}

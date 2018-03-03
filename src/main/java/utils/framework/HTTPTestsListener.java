package utils.framework;

import java.io.IOException;
import io.restassured.response.ValidatableResponse;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.api.Authorization;

import java.lang.reflect.Method;
import java.util.Map;
import utils.TestCase;
import utils.api.JiraApiActions;
import utils.data.JiraJsonObjectHelper;
import utils.test_management_system.TestRailAPIClient;
import utils.test_management_system.TestRailApiException;

public class HTTPTestsListener implements ITestListener {

  final static PropertyReader propertyReader = new PropertyReader();
  public static Map<String, String> propertiesJira = propertyReader
      .readProperties("jira.properties");
  public static Map<String, String> propertiesTestRail = propertyReader
      .readProperties("testrail.properties");
  static final Logger logger = Logger.getLogger(HTTPTestsListener.class);

  public void onTestStart(ITestResult iTestResult) {
    logger.info("===== '" + iTestResult.getName() + "' test started =====");
    String JiraId = getJiraAnnotation(iTestResult);
    String testCaseId = getTestCaseId(iTestResult);
    logger.info("Jira id: " + JiraId);
    logger.info("Test Case id: " + testCaseId);
  }

  public void onTestSuccess(ITestResult iTestResult) {
//    updateTestRun("1","1");
    updateJiraTicketStatus(true, iTestResult);
  }

  public void onTestFailure(ITestResult iTestResult) {
//    updateTestRun("1", "1");
      updateJiraTicketStatus(false, iTestResult);
  }

  public void onTestSkipped(ITestResult iTestResult) {

  }

  public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

  }

  public void onStart(ITestContext iTestContext) {
    logger.info("===== 'Authorization.loginToJIRA' started =====");
    Authorization.loginToJIRA();
  }

  public void onFinish(ITestContext iTestContext) {

  }

  public String getTestCaseId(ITestResult iTestResult) {
    String id = null;
    Method method = iTestResult.getMethod().getConstructorOrMethod().getMethod();
    try {
      TestCase testCaseAnnotation = method.getAnnotation(TestCase.class);
      id = testCaseAnnotation.id();
      logger.debug("ANNOTATION: " + testCaseAnnotation);
    } catch (NullPointerException e) {
      logger.debug("There is no @TestCase annotation over this method");
    }
    return id;
  }

  public String getJiraAnnotation(ITestResult iTestResult) {
    String id = null;
    Method method = iTestResult.getMethod().getConstructorOrMethod().getMethod();
    try {
      JiraAnnotation testJiraAnnotation = method
          .getAnnotation(JiraAnnotation.class); // Где бы я не выполнялся, Java верни
      // аннотацию из метода в котором я выполняюсь. Похожим образом можно сделать для класса.
      id = testJiraAnnotation.id();
      logger.debug("ANNOTATION: " + testJiraAnnotation);
    } catch (NullPointerException e) {
      logger.debug("There is no @JiraAnnotation over this method");
    }
    return id;
  }

  private void updateTestRun(String testId, String statusId) {

    String testRunId = "";
    String pathTrail = "";
    String userTrail = "";
    String passwordTrail = "";

    testRunId = propertiesTestRail.get("test_run_id");
    pathTrail = propertiesTestRail.get("path_t");
    userTrail = propertiesTestRail.get("user_trail");
    passwordTrail = propertiesTestRail.get("password_trail");

    TestRailAPIClient client = new TestRailAPIClient(pathTrail);
    client.setUser(userTrail);
    client.setPassword(passwordTrail);

    JSONObject response = null;
    JSONObject body = new JSONObject();
    body.put("status_id", statusId);

    try {
      response = (JSONObject) client
          .sendPost("add_result_for_case/" + testRunId + "/" + testId, body);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (TestRailApiException e) {
      e.printStackTrace();
    }


  }

  private void updateJiraTicketStatus(boolean isTestPassed, ITestResult iTestResult) {
      String jiraIssueKey = getJiraAnnotation(iTestResult);
      ValidatableResponse receivedIssue = null;
      String jiraIssueStatus = null;

      if (jiraIssueKey != null && !jiraIssueKey.equals("")) { // ID in annotation exists
          try {
              receivedIssue = JiraApiActions.getIssue(jiraIssueKey);
              jiraIssueStatus = receivedIssue.extract().jsonPath().get("fields.status.name");
              logger.debug("Current issue status:" + jiraIssueStatus);
          } catch (AssertionError error){
              logger.debug(error.getMessage());
              logger.warn("Ticket " + jiraIssueKey + " was not found, PLEASE DELETE IT FROM @JiraAnnotation");
          }
      }
      if (receivedIssue != null) { // exist
          if (isTestPassed) { // passed
              if (jiraIssueStatus.equals("Done")) { // closed
                  // do nothing
              } else { // open ( "Backlog", "In Progress", "Selected for Development")
                  // TODO -- close & add comment "fixed"
                  logger.info("Isuue " + jiraIssueKey + " closed with comment \"fixed\"");
              }
          } else { //  failed
              if (jiraIssueStatus.equals("Done")) { // closed
                  //TODO -- reopen & add comment
                  logger.info("Isuue " + jiraIssueKey + " reopened with comment \"reopened\"");
              } else { // open
                  //TODO -- add comment "still reproduced"
                  logger.info("Isuue " + jiraIssueKey + " left open with comment \"still reproduced\"");
              }
          }
      } else { // not exist
          if (isTestPassed == false) { // Test Failed
              //  Create new issue and add new comment to it
              ValidatableResponse createIssueResponse = JiraApiActions.createIssue(JiraJsonObjectHelper.generateJSONForIssue("10502",iTestResult.getMethod().getQualifiedName(),"10107","Artur Piluck"));
              String newIssueId = createIssueResponse.extract().path("key").toString();
              JiraApiActions.createComment(newIssueId, "Add new test commemt");

              // TODO Add Summary with failed test -done
              // TODO and description with logs
//              String newIssueId = "QAAUT-1000"; // TODO get this from Jira's response on successful issue creation
              logger.info("New Issue " + newIssueId +" created.");
              logger.info("PLEASE ADD THIS CODE: @JiraAnnotation(id = \"" + newIssueId + "\") " +
                              "ABOVE " + iTestResult.getMethod().getQualifiedName() + "() METHOD");
          } else { // Test Passed
              // Nothing to do.
          }
      }

  }
}

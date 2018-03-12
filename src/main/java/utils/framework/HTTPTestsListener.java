package utils.framework;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import utils.api.Authorization;
import java.lang.reflect.Method;
import java.util.Map;
import utils.TestCase;
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
    String testCaseId = getTestCaseId(iTestResult);
    logger.info("Test Case id: " + testCaseId);
  }
    public void onTestSuccess(ITestResult iTestResult) {
        String testCaseName = iTestResult.getName();
        logger.info("TEST: " + testCaseName + " PASSED");
        String testCaseId = getTestCaseId(iTestResult);
        updateTestRun(testCaseId, "1");
        JiraAnnotationManager.updateJiraTicketStatus(iTestResult);
    }

    public void onTestFailure(ITestResult iTestResult) {
        logger.error("TEST: " + iTestResult.getName() + " FAILED");
        logger.error(iTestResult.getThrowable().fillInStackTrace());
        // TODO add option to enable / disable post of results
        String testCaseId = getTestCaseId(iTestResult);
        // updateTestRun(testCaseId, "5");
        JiraAnnotationManager.updateJiraTicketStatus(iTestResult);
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
}

package utils.framework;

import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.api.Authorization;

import java.lang.reflect.Method;
import java.util.Map;
import utils.TestCase;

public class HTTPTestsListener implements ITestListener {

  final static PropertyReader propertyReader = new PropertyReader();
  public static Map<String, String> propertiesJira = propertyReader
          .readProperties("jira.properties");
  public static Map<String, String> propertiesTestRail = propertyReader
          .readProperties("testrail.properties");
  static final Logger logger = Logger.getLogger(HTTPTestsListener.class);

  public void onTestStart(ITestResult iTestResult) {
      logger.info("===== '" + iTestResult.getName()+ "' test started =====");
      String JiraId = getJiraAnnotation(iTestResult);
      String testCaseId = getTestCaseId(iTestResult);
      logger.info("Jira id: " + JiraId);
      logger.info("Test Case id: " + testCaseId);
  }

  public void onTestSuccess(ITestResult iTestResult) {

  }

  public void onTestFailure(ITestResult iTestResult) {

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

  public String getJiraAnnotation (ITestResult iTestResult) {
      String id = null;
      Method method = iTestResult.getMethod().getConstructorOrMethod().getMethod();
      try {
          JiraAnnotation testJiraAnnotation = method.getAnnotation(JiraAnnotation.class); // Где бы я не выполнялся, Java верни
          // аннотацию из метода в котором я выполняюсь. Похожим образом можно сделать для класса.
          id = testJiraAnnotation.id();
          logger.debug("ANNOTATION: " + testJiraAnnotation);
      } catch (NullPointerException e) {
          logger.debug("There is no @JiraAnnotation over this method");
      }
      return id;
  }
}

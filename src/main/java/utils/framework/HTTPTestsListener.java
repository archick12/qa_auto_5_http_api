package utils.framework;

import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.api.Authorization;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

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
      logger.info("Jira id: " + JiraId);
      String AssinneeId = getAssigneeAnnotation(iTestResult);
      logger.info("Assignee id: " + AssinneeId);
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
    Authorization.loginToJIRA();
  }

  public void onFinish(ITestContext iTestContext) {

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
    public String getAssigneeAnnotation (ITestResult iTestResult) {
        String id = null;
        Method method = iTestResult.getMethod().getConstructorOrMethod().getMethod();
        try {
            AssinneeAnnotation testAssigneeAnnotation = method.getAnnotation(AssinneeAnnotation.class); // Где бы я не выполнялся, Java верни
            // аннотацию из метода в котором я выполняюсь. Похожим образом можно сделать для класса.
            id = testAssigneeAnnotation.asigneeId();
            logger.debug("ANNOTATION: " + testAssigneeAnnotation);
        } catch (NullPointerException e) {
            logger.debug("There is no @JiraAnnotation over this method");
        }
        return id;
    }
}

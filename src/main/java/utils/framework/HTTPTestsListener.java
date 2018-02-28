package utils.framework;
import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.api.Authorization;

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

      Class myClass = iTestResult.getTestClass().getRealClass();
      Method method = null;
      String id = null;
      String methodName = iTestResult.getMethod().getMethodName();
      try {
          method = myClass.getMethod(methodName); // спрашиваем Java: "Как называется, метод внутри которого
          // ты сейчас выполняешь этот кусочек кода". Проще говоря - "Где я выполнился?"
      } catch (NoSuchMethodException e) {
          try {
              method = myClass.getMethod(methodName, String.class);
          } catch (NoSuchMethodException e1) {
              e1.printStackTrace();
          }
      }
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

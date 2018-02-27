package utils.framework;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.api.Authorization;
import utils.api.JiraAnnotation;

import java.lang.reflect.Method;
import java.util.Map;

public class HTTPTestsListener implements ITestListener {

  final static PropertyReader propertyReader = new PropertyReader();
  public static Map<String, String> properties = propertyReader
          .readProperties("jira.properties");


  public void onTestStart(ITestResult result) {
      JiraAnnotation(result);

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

  public void JiraAnnotation (ITestResult result){

    Class myClass = result.getTestClass().getRealClass();
    Method method = null; //
    try {
      String methodName = result.getMethod().getMethodName();
      method = myClass.getMethod(methodName); // спрашиваем Java: "Как называется, метод внутри которого
      // ты сейчас выполняешь этот кусочек кода". Проще говоря - "Где я выполнился?"
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }

    JiraAnnotation testJiraAnnotation = method.getAnnotation(JiraAnnotation.class); // Где бы я не выполнялся, Java верни
    // аннотацию из метода в котором я выполняюсь. Похожим образом можно сделать для класса.
    System.out.println("ANNOTATION: " + testJiraAnnotation);
    System.out.println("Jira id: " + testJiraAnnotation.id());
    System.out.println("Jira comment: " + testJiraAnnotation.comment()); // верни значение внутри аннотации
  }
}

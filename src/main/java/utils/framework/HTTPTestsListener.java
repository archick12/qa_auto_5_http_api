package utils.framework;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.api.Authorization;
import java.util.Map;

public class HTTPTestsListener implements ITestListener {

  final static PropertyReader propertyReader = new PropertyReader();
  public static Map<String, String> properties = propertyReader
          .readProperties("jira.properties");


  public void onTestStart(ITestResult iTestResult) {
      // TODO
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
}

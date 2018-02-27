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
    public static Map<String, String> properties = propertyReader
            .readProperties("jira.properties");
    static final Logger logger = Logger.getLogger(HTTPTestsListener.class);


    public void onTestStart(ITestResult iTestResult) {
        // TODO
    }

    public void onTestSuccess(ITestResult iTestResult) {
        String testCaseId = getTestCaseId(iTestResult);
        logger.info("Test Case id: " + testCaseId);
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

    public String getTestCaseId(ITestResult iTestResult) {
        Class myClass = iTestResult.getTestClass().getRealClass();
        Method method = null;
        try {
            String methodName = iTestResult.getMethod().getMethodName();
            method = myClass.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        TestCase testCaseAnnotation = method.getAnnotation(TestCase.class);
        logger.debug("ANNOTATION: " + testCaseAnnotation);
        return testCaseAnnotation.id();
    }
}

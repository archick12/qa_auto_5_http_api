import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.lang.reflect.Method;

public class  TestListener implements ITestListener {
    public void onTestStart(ITestResult result) {

    }

    public void onTestSuccess(ITestResult result) {
        printJiraId(result);
        printComment(result);

    }

    public void onTestFailure(ITestResult result) {

    }

    public void onTestSkipped(ITestResult result) {

    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    public void onStart(ITestContext context) {

    }

    public void onFinish(ITestContext context) {

    }

    public void printJiraId(ITestResult result){
          /*
        * Вот этот блок кода можно применить что бы получить аннотации над методом.
        */
        // Class myClass = SampleEmptyTest.class; заменяем на обращение к ITestResult - TestNG в нем хранит информацию о методах и результатах
        Class myClass = result.getTestClass().getRealClass();
        Method method = null; //
        try {
            String methodName = result.getMethod().getMethodName();
            method = myClass.getMethod(methodName); // спрашиваем Java: "Как называется, метод внутри которого
            // ты сейчас выполняешь этот кусочек кода". Проще говоря - "Где я выполнился?"
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        JiraAnnotation testCaseJiraAnnotation = method.getAnnotation(JiraAnnotation.class); // Где бы я не выполнялся, Java верни
        // аннотацию из метода в котором я выполняюсь. Похожим образом можно сделать для класса.
        System.out.println("ANNOTATION: " + testCaseJiraAnnotation);
        System.out.println("Jira id: " + testCaseJiraAnnotation.id()); // верни значение внутри аннотации
    }
    public void printComment(ITestResult result){
        Class myClass = result.getTestClass().getRealClass();
        Method method = null;
        try {
            String methodName = result.getMethod().getMethodName();
            method = myClass.getMethod(methodName); // спрашиваем Java: "Как называется, метод внутри которого
            // ты сейчас выполняешь этот кусочек кода". Проще говоря - "Где я выполнился?"
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        JiraAnnotationComment testCaseCommentJiraAnnotation = method.getAnnotation(JiraAnnotationComment.class);
        System.out.println("ANNOTATION" + testCaseCommentJiraAnnotation);
        System.out.println("Jira comment" + testCaseCommentJiraAnnotation.comment());
    }

}


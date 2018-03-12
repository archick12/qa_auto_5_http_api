package utils.framework;
import io.restassured.response.ValidatableResponse;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestResult;
import utils.api.APIPathes;
import utils.api.HTTPMethods;
import utils.data.JiraJsonObjectHelper;

import java.lang.reflect.Method;

public class JiraAnnotationManager {

    static final Logger logger = Logger.getLogger(JiraAnnotationManager.class);

    // Jira ticket status
    private final static String BACKLOG = "11";
    private final static String SELECTEDFORDEVELOPMENT = "21";
    private final static String INPROGRESS = "31";
    private final static String DONE = "41";

    private static String requestBodyForClosingIssue = JiraJsonObjectHelper.generateJSONForIssueTransition(DONE);
    private static String requestBodyForReopeningIssue = JiraJsonObjectHelper.generateJSONForIssueTransition(SELECTEDFORDEVELOPMENT);
    private static String commentTestPassed = JiraJsonObjectHelper.generateJSONForComment("The test passed");
    private static String commentTestFailed = JiraJsonObjectHelper.generateJSONForComment("The test failed");

    public static String getIssueStatus(String issueKey) {
        try {
            ValidatableResponse response = HTTPMethods.get(APIPathes.issue + issueKey);
            Assert.assertEquals(response.extract().statusCode(), 200);
            String statusName = response.extract().jsonPath().get("fields.status.name");
            return statusName;
        } catch (AssertionError error) {
            logger.error("===== Failed to get the ticket status =====", error);
            return null;
        }
    }

    public static boolean doesIssueExist(String issueKey) {
        try {
            ValidatableResponse response = HTTPMethods.get(APIPathes.issue + issueKey);
            Assert.assertEquals(response.extract().statusCode(), 200);
            return true;
        } catch (AssertionError error) {
            logger.error("===== The ticket doesn't exist =====", error);
            return false;
        }
    }

    public static String getJiraAnnotation(ITestResult result) {
        Method method = result.getMethod().getConstructorOrMethod().getMethod();
        try {
            JiraAnnotation jiraAnnotation = method.getAnnotation(JiraAnnotation.class);
            String id = jiraAnnotation.id();
            logger.debug("ANNOTATION: " + jiraAnnotation);
            return id;
        } catch (NullPointerException e) {
            logger.error("===== There is no @JiraAnnotation over this method =====", e);
            return null;
        }
    }

    public static void closeTicket(String issueID) {
        try {
            ValidatableResponse response = HTTPMethods.post(String.format(APIPathes.issueTransitions, issueID), requestBodyForClosingIssue);
            Assert.assertEquals(response.extract().statusCode(), 204);
        } catch (AssertionError error) {
            logger.error("===== Failed to close a ticket =====", error);
        }
    }

    public static void reopenTicket(String issueID) {
        try {
            ValidatableResponse response1 = HTTPMethods.post(String.format(APIPathes.issueTransitions, issueID), requestBodyForReopeningIssue);
            Assert.assertEquals(response1.extract().statusCode(), 204);
        } catch (AssertionError error) {
            logger.error("===== Failed to reopen a ticket =====", error);
        }
    }

    public static void setCommentTestPassed(String issueID) {
        try {
            ValidatableResponse response = HTTPMethods.post(String.format(APIPathes.commentInIssue, issueID), commentTestPassed);
            Assert.assertEquals(response.extract().statusCode(), 201);
        } catch (AssertionError error) {
            logger.error("===== Failed to comment a ticket =====", error);
        }
    }

    public static void setCommentTestFailed(String issueID) {
        try {
            ValidatableResponse response = HTTPMethods.post(String.format(APIPathes.commentInIssue, issueID), commentTestFailed);
            Assert.assertEquals(response.extract().statusCode(), 201);
        } catch (AssertionError error) {
            logger.error("===== Failed to comment a ticket =====", error);
        }
    }

    public static String createNewTicket(ITestResult result) {
        String projectID = "10502";
        String issueSummary = result.getName();
        String issueType = "10107";
        String issueAssignee = HTTPTestsListener.propertiesJira.get("username");
        String newIssueJSON = JiraJsonObjectHelper.generateJSONForIssue(projectID, issueSummary, issueType, issueAssignee);
        try {
            ValidatableResponse response = HTTPMethods.post(APIPathes.issue, newIssueJSON);
            Assert.assertEquals(response.extract().statusCode(), 201);
            String newIssueKey = response.extract().path("key");
            return newIssueKey;
        } catch (AssertionError error) {
            logger.error("===== Failed to create a ticket =====", error);
            return null;
        }
    }

    public static void updateJiraTicketStatus(ITestResult iTestResult) {
        String testJiraAnnotation = getJiraAnnotation(iTestResult);
        boolean isIssueReal = doesIssueExist(testJiraAnnotation);
        String jiraIssueStatus = getIssueStatus(testJiraAnnotation);
        logger.info("Current issue status:" + jiraIssueStatus);

        if (testJiraAnnotation != null && isIssueReal && iTestResult.isSuccess()) {
            // test passed, annotation exists, ticket exists, ticket is closed
            if (jiraIssueStatus.equals("Done")) {
                logger.info("Isuue " + testJiraAnnotation + " is already closed");
            // test passed, annotation exists, ticket exists, ticket is opened ( "Backlog", "In Progress", "Selected for Development")
            } else {
                closeTicket(testJiraAnnotation);
                setCommentTestPassed(testJiraAnnotation);
                logger.info("Isuue " + testJiraAnnotation + " closed with comment \"fixed\"");
            }
        }

        else if (testJiraAnnotation != null && isIssueReal && !iTestResult.isSuccess()) {
            // test failed, annotation exists, ticket exists, ticket is closed
            if (jiraIssueStatus.equals("Done")) { // closed
                reopenTicket(testJiraAnnotation);
                setCommentTestFailed(testJiraAnnotation);
                logger.info("Isuue " + testJiraAnnotation + " was reopened");
             // test failed, annotation exists, ticket exists, ticket is opened ( "Backlog", "In Progress", "Selected for Development")
            } else {
                setCommentTestFailed(testJiraAnnotation);
                logger.info("Isuue " + testJiraAnnotation + " left open with comment \"still reproduced\"");
            }
        }
        // test failed, annotation does't exist
        else if (testJiraAnnotation == null && !iTestResult.isSuccess()) {
            String newIssueKey = createNewTicket(iTestResult);
                logger.info("New Issue " + newIssueKey +" created.");
                logger.info("PLEASE ADD THIS CODE: @JiraAnnotation(id = \"" + newIssueKey + "\") " +
                        "ABOVE " + iTestResult.getName() + "() METHOD");
            // TODO and description with logs

        } else {logger.error("Unhandled scenario just happened");}

    }

}

import io.restassured.response.ValidatableResponse;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.apache.log4j.Logger;
import utils.TestCase;
import utils.api.Authorization;
import utils.api.JiraApiActions;
import utils.framework.JiraAnnotation;

import java.util.List;

import static org.testng.Assert.*;

public class RestAPISearchTests {

    static final Logger logger = Logger.getLogger(RestAPISearchTests.class);

    @Test(groups = {"CRITICAL", "HTTP"})
    public void authentication() {
        assertNotNull(Authorization.JSESSIONID);
    }

    @TestCase(id = "1")
    @JiraAnnotation(id = "QAAUT-494")
    @Test(groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
    public void searchByProject() {
    /* HTTP Request for search for issues by project*/
        String projectName = "QAAuto5";
        int counter = 0;
        ValidatableResponse response = JiraApiActions.searchForIssues("project = " + projectName);
        List<String> searchResultIssuesProjectName = response.extract().jsonPath()
                .getList("issues.fields.project.name");
        for (String item : searchResultIssuesProjectName) {
            assertEquals(true, item.contains(projectName));
            counter++;
        }
        logger.info("Assertion passed for '" + projectName + "' project " + counter + " times");
    }

    @TestCase(id = "2")
    @JiraAnnotation(id = "QAAUT-494")
    @Test(groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
    public void searchByCurrentUserAsAssignee() {
    /* HTTP Request for search for issues by Assignee - Current User*/
        String assignee = Authorization.username;
        int counter = 0;
        ValidatableResponse response = JiraApiActions.searchForIssues("assignee = " + assignee);
        List<String> searchResultIssuesAssignee = response.extract().jsonPath()
                .getList("issues.fields.assignee.name");
        for (String item : searchResultIssuesAssignee) {
            assertEquals(true, item.contains(assignee));
            counter++;
        }
        logger.info("Assertion passed for '" + assignee + "' assignee " + counter + " times");
    }

    @TestCase(id = "3")
    @JiraAnnotation(id = "QAAUT-494")
    @Test(groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
    public void searchByUnassignedAsAssignee() {
    /* HTTP Request for search for issues by Assignee - Unassigned*/
        String assignee = "Unassigned";
        int counter = 0;
        ValidatableResponse response = JiraApiActions.searchForIssues("assignee = " + assignee);
        List<String> searchResultIssuesAssignee = response.extract().jsonPath()
                .getList("issues.fields.assignee.name");
        for (String item : searchResultIssuesAssignee) {
            assertEquals(true, item.contains(assignee));
            counter++;
        }
        logger.info("Assertion passed for '" + assignee + "' assignee " + counter + " times");
    }

    @DataProvider
    public Object[][] getIssueTypesData() {
        return new Object[][]{{"Bug"}, {"Story"}, {"Epic"}, {"Improvement"}, {"Task"}, {"Sub-task"}, {"Sub-Defect"}};
    }

    @TestCase(id = "4")
    @JiraAnnotation(id = "QAAUT-494")
    @Test(dataProvider = "getIssueTypesData", groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
    public void searchIssuesByDifferentType(String type) {
        int counter = 0;
        ValidatableResponse response = JiraApiActions.searchForIssues("issuetype = " + type);
        List<String> searchResult = response.extract().jsonPath()
                .getList("issues.fields.issuetype.name");
        for (String item : searchResult) {
            assertEquals(true, item.contains(type));
            counter++;
        }
        logger.info("Assertion passed for '" + type + "' type " + counter + " times");
    }

////    тестовый тест для дебага
//    @TestCase(id = "C15")
//    @JiraAnnotation(id = "QAAUT-4000")
//    @Test(groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
//    public void createIsuueTestForDebug() {
//        assertTrue(false);
//    }

}

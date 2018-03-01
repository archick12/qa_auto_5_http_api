import io.restassured.response.ValidatableResponse;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.apache.log4j.Logger;
import utils.TestCase;
import utils.api.Authorization;
import utils.api.JiraApiActions;
import utils.framework.JiraAnnotation;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class RestAPISearchTests {

    static final Logger logger = Logger.getLogger(RestAPISearchTests.class);

    @Test(groups = {"CRITICAL", "HTTP"})
    public void authentication() {
        assertNotNull(Authorization.JSESSIONID);
    }

    @TestCase(id = "C1")
    @JiraAnnotation(id = "QAAUT-494")
    @Test(groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
    public void searchByProject() {
    /* HTTP Request for search for issues by project*/
        String projectName = "QAAuto5";
        ValidatableResponse response = JiraApiActions.searchForIssues("project = " + projectName);
        List<String> searchResultIssuesProjectName = response.extract().jsonPath()
                .getList("issues.fields.project.name");
        for (String item : searchResultIssuesProjectName) {
            assertEquals(true, item.contains(projectName));
            logger.info("Assertion passed: " + item);
        }
    }

    @TestCase(id = "C2")
    @JiraAnnotation(id = "QAAUT-494")
    @Test(groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
    public void searchByCurrentUserAsAssignee() {
    /* HTTP Request for search for issues by Assignee - Current User*/
        String assignee = Authorization.username;
        ValidatableResponse response = JiraApiActions.searchForIssues("assignee = " + assignee);
        List<String> searchResultIssuesAssignee = response.extract().jsonPath()
                .getList("issues.fields.assignee");
        for (String item : searchResultIssuesAssignee) {
            assertEquals(true, item.contains(assignee));
            logger.info("Assertion passed: " + item);
        }
    }

    @TestCase(id = "C3")
    @JiraAnnotation(id = "QAAUT-494")
    @Test(groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
    public void searchByUnassignedAsAssignee() {
    /* HTTP Request for search for issues by Assignee - Unassigned*/
        String assignee = "Unassigned";
        ValidatableResponse response = JiraApiActions.searchForIssues("assignee = " + assignee);
        List<String> searchResultIssuesAssignee = response.extract().jsonPath()
                .getList("issues.fields.assignee");
        for (String item : searchResultIssuesAssignee) {
            assertEquals(true, item.contains(assignee));
            logger.info("Assertion passed: " + item);
        }
    }

    @DataProvider
    public Object[][] getIssueTypesData() {
        return new Object[][]{{"Bug"}, {"Story"}, {"Epic"}, {"Improvement"}, {"Task"}, {"Sub-task"}, {"Sub-Defect"}};
    }

    @TestCase(id = "C4")
    @JiraAnnotation(id = "QAAUT-494")
    @Test(dataProvider = "getIssueTypesData", groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
    public void searchIssuesByDifferentType(String type) {
        ValidatableResponse response = JiraApiActions.searchForIssues("issuetype = " + type);
        List<String> searchResult = response.extract().jsonPath()
                .getList("issues.fields.issuetype.name");
        for (String item : searchResult) {
            assertEquals(true, item.contains(type));
        }
    }
}

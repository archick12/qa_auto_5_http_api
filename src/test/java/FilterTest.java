import org.testng.annotations.Test;
import utils.TestCase;
import utils.api.*;

import static utils.api.JiraApiActions.createFilter;
import static utils.api.JiraApiActions.getFilter;
import static utils.api.JiraApiActions.deleteFilter;
import static utils.api.JiraApiActions.getDeletedFilter;
import static utils.api.JiraApiActions.updateFilter;
import static utils.api.JiraApiActions.setFavouriteFlag;
import static utils.api.JiraApiActions.deleteFavouriteFlag;
import static utils.api.JiraApiActions.getFilterPermission;
import static utils.api.JiraApiActions.getAllFilterPermissions;
import static utils.api.JiraApiActions.addFilterPermission;
import static utils.api.JiraApiActions.deleteFilterPermission;
import static utils.data.JiraJsonObjectHelper.generateJSONForNewFilter;
import static utils.data.JiraJsonObjectHelper.generateJSONForFilterName;
import static utils.data.JiraJsonObjectHelper.generateJSONForFilterJQL;
import static utils.data.JiraJsonObjectHelper.generateJSONForFilterPermission;

public class FilterTest {

  // vars for existing test filter
  private final String testFilterID = "10904";

  @Test(groups = {"CRITICAL", "HTTP"})
  public void authenticate() {
    Authorization.loginToJIRA();
  }

  @TestCase(id = "C8")
  @Test(groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
  public void createNewFilter() {
    String newFilterName = "First filter";
    String newFilterDescription = "Filter for test";
    String newFilterJQL = "project = QAAUT AND issuetype = Story AND status = Backlog AND priority = Medium";
    boolean newFilterFavouriteFlag = true;

    String json = generateJSONForNewFilter(newFilterName, newFilterDescription,
        newFilterJQL, newFilterFavouriteFlag);

    String newFilterID = createFilter(json).extract().path("id").toString();
    getFilter(newFilterID);
    deleteFilter(newFilterID);
    getDeletedFilter(newFilterID);
  }

  @TestCase(id = "C9")
  @Test(groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
  public void updateFilterName() {
    String initialName = "Alesya test filter";
    String newName = "Alesya test filter UPDATED";
    // update filter name
    updateFilter(testFilterID, generateJSONForFilterName(newName));
    // update filter name to its initial value
    updateFilter(testFilterID, generateJSONForFilterName(initialName));
  }

  @TestCase(id = "C10")
  @Test(groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
  public void updateFilterJQL() {
    String initialJQL = "project = QAAUT AND issuetype = Story AND status = Backlog AND priority = Medium";
    String newJQL = "project = QAAUT AND issuetype = Bug AND status = Backlog";
    // update filter jql
    updateFilter(testFilterID, generateJSONForFilterJQL(newJQL));
    // update filter jql to its initial value
    updateFilter(testFilterID, generateJSONForFilterJQL(initialJQL));
  }

  // TODO currently this test always returns code 404, need to fix it(don't know how)
  @TestCase(id = "C11")
  @Test(groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
  public void addFilterToFavourite() {
    String filterFavouriteRequestEmptyBody = "";

    setFavouriteFlag(testFilterID, filterFavouriteRequestEmptyBody);
    deleteFavouriteFlag(testFilterID);
  }

  @TestCase(id = "C12")
  @Test(groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
  public void setFilterPermissions() {
    String newPermissionType = "group";
    String newPermissionGroupname = "jira-software-users";

    String newPermissionID = addFilterPermission(testFilterID,
        generateJSONForFilterPermission(newPermissionType, newPermissionGroupname)).extract()
        .path("id").toString().replace("[", "").replace("]", "");
    //  TODO response.extract().jsonPath().getList("issues.fields.assignee");
    getFilterPermission(testFilterID, newPermissionID);
    deleteFilterPermission(testFilterID, newPermissionID);
  }
}

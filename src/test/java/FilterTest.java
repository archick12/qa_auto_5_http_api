import org.testng.annotations.Test;
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

public class FilterTest implements APIPathes {

    // vars for new filter
    private String newFilterName = "First filter";
    private String newFilterDescription = "Filter for test";
    private String newFilterJQL = "project = QAAUT AND issuetype = Story AND status = Backlog AND priority = Medium";
    private boolean newFilterFavouriteFlag = true;
    // vars for existing test filter
    private final String testFilterID = "10904";
    private String initialName = "Alesya test filter";
    private String newName = "Alesya test filter UPDATED";
    private String initialJQL = "project = QAAUT AND issuetype = Story AND status = Backlog AND priority = Medium";
    private String newJQL = "project = QAAUT AND issuetype = Bug AND status = Backlog";
    private String filterFavouriteRequestEmptyBody = "";
    private String newPermissionType = "group";
    private String newPermissionGroupname = "jira-software-users";

    @Test(groups = {"CRITICAL", "HTTP"})
    public void authenticate() { Authorization.loginToJIRA();}

    @Test(groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
    public void createNewFilter() {
       String newFilterID = createFilter(generateJSONForNewFilter(newFilterName, newFilterDescription, newFilterJQL, newFilterFavouriteFlag)).extract().path("id").toString();
        getFilter(newFilterID);
        deleteFilter(newFilterID);
        getDeletedFilter(newFilterID);
    }

    @Test(groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
    public void updateFilterName() {
        // update filter name
        updateFilter(testFilterID, generateJSONForFilterName(newName));
        // update filter name to its initial value
        updateFilter(testFilterID, generateJSONForFilterName(initialName));
    }

    @Test(groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
    public void updateFilterJQL() {
        // update filter jql
        updateFilter(testFilterID, generateJSONForFilterJQL(newJQL));
        // update filter jql to its initial value
        updateFilter(testFilterID, generateJSONForFilterJQL(initialJQL));
    }

    // TODO currently this test always returns code 404, need to fix it(don't know how)
//    @Test(groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
//    public void addFilterToFavourite() {
//        setFavouriteFlag(testFilterID, filterFavouriteRequestEmptyBody);
//        deleteFavouriteFlag(testFilterID);
//    }

    @Test(groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
    public void setFilterPermissions() {
        String newPermissionID = addFilterPermission(testFilterID, generateJSONForFilterPermission(newPermissionType, newPermissionGroupname)).extract().path("id").toString().replace("[", "").replace("]", "");
        getFilterPermission(testFilterID, newPermissionID);
        deleteFilterPermission(testFilterID, newPermissionID);
    }
}

import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;
import utils.api.Authorization;
import utils.api.JiraApiActions;


public class FilterTests {

   private String filterJSON = "{" + "\"name\": \"Filter4\"," +
            "\"description\": \"Filter for test\"," +
            "\"jql\": \"project = QAAUT AND issuetype = Story AND status = Backlog AND priority = Medium\"," +
            "\"favourite\": true" +
            "}";


    @Test(groups = {"CRITICAL", "HTTP"})
    public void authentication() {
        Authorization.loginToJIRA();
    }

    @Test(groups = {"Regression", "HTTP"}, dependsOnGroups = {"CRITICAL"})
    public void createFilter() {
        /* create */
        ValidatableResponse response = JiraApiActions.createFilter(filterJSON);
        String filterId = response.extract().path("id").toString();

        /* confirm create */ // как правильно сравнить, что мы получили тот айди, который автоматом создался
        JiraApiActions.getFilter(filterId);

        /* delete */
        JiraApiActions.deleteFilter(filterId);

        /* confirm delete*/
        JiraApiActions.getDeletedFilter(filterId);
    }

}
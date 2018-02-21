import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

public class FilterTest {

    private final String credentialsJSON = "{" +
            "\"username\": \"qa_auto_5_team_2\"," +
            "\"password\": \"qa_auto_5_team_2\"" +
            "} ";
    private String filterJSON = "{" + "\"name\": \"First filter\"," +
            "\"description\": \"Filter for test\"," +
            "\"jql\": \"project = QAAUT AND issuetype = Story AND status = Backlog AND priority = Medium\"," +
            "\"favourite\": true" +
            "}";
    private final String testFilterID = "10904";
    private String initialNameJSON = "{" +
            "\"name\": \"Alesya test filter\"" +
            "} ";
    private String newNameJSON = "{" +
            "\"name\": \"Alesya test filter UPDATED\"" +
            "} ";
    private String initialJQL = "{" +
            "\"jql\": \"project = QAAUT AND issuetype = Story AND status = Backlog AND priority = Medium\"" +
            "} ";
    private String newJQL = "{" +
            "\"jql\": \"project = QAAUT AND issuetype = Bug AND status = Backlog\"" +
            "} ";
    private String newPermission = "{" +
            "\"type\": \"group\"," +
            "\"groupname\": \"jira-software-users\"" +
            "}";

    public String getSessionId() {
        RestAssured.baseURI = "http://jira.hillel.it:8080"; // rest assured preconfiguration
        String sessionId = given().
            header("Content-Type", "application/json").
            body(credentialsJSON).
            when().
            post("/rest/auth/1/session").
            then().
            extract().
            path("session.value");
        return sessionId;
    };

    public void getFilter(String filterID) {
        System.out.println("-----------------------------GETTING FILTER BY ID-----------------------------");
        RestAssured.baseURI = "http://jira.hillel.it:8080"; // rest assured preconfiguration
        given().
            header("Content-Type", "application/json").
            header("Cookie", "JSESSIONID=" + getSessionId()).
            when().
            get("/rest/api/2/filter/" + filterID).
            then().
            statusCode(200).
            log().status().
            log().body();
    };

    public String createFilterAndGetID(String requestBody) {
        System.out.println("-----------------------------CREATING NEW FILTER-----------------------------");
        RestAssured.baseURI = "http://jira.hillel.it:8080"; // rest assured preconfiguration
        String newFilterID = given().
            header("Content-Type", "application/json").
            header("Cookie", "JSESSIONID=" + getSessionId()).
            body(requestBody).
            when().
            post("/rest/api/2/filter").
            then().
            statusCode(200).
            log().status().
            log().body().
            extract().path("id");
        return newFilterID;
    };

    public void updateFilter(String requestBody, String filterID) {
        System.out.println("-----------------------------UPDATING FILTER-----------------------------");
        RestAssured.baseURI = "http://jira.hillel.it:8080"; // rest assured preconfiguration
        given().
            header("Content-Type", "application/json").
            header("Cookie", "JSESSIONID=" + getSessionId()).
            body(requestBody).
            when().
            put("/rest/api/2/filter/" + filterID).
            then().
            statusCode(200).
            log().status().
            log().body();
    }

    public void deleteFilter(String filterID) {
        System.out.println("-----------------------------DELETING FILTER BY ID-----------------------------");
        RestAssured.baseURI = "http://jira.hillel.it:8080"; // rest assured preconfiguration
        given().
            header("Content-Type", "application/json").
            header("Cookie", "JSESSIONID=" + getSessionId()).
            when().
            delete("/rest/api/2/filter/" + filterID).
            then().
            statusCode(204).
            log().status().
            log().body();
    };

    public void setFavouriteFlag(String filterID) {
        System.out.println("-----------------------------SETTING FILTER FAVOURITE FLAG-----------------------------");
        RestAssured.baseURI = "http://jira.hillel.it:8080"; // rest assured preconfiguration
        given().
            header("Content-Type", "application/json").
            header("Cookie", "JSESSIONID=" + getSessionId()).
            when().
            put("/rest/api/2/filter/" + filterID + "/favourite").
            then().
            statusCode(200).
            log().status().
            log().body();
    }

    public void deleteFavouriteFlag(String filterID) {
        System.out.println("-----------------------------DELETING FILTER FAVOURITE FLAG-----------------------------");
        RestAssured.baseURI = "http://jira.hillel.it:8080"; // rest assured preconfiguration
        given().
                header("Content-Type", "application/json").
                header("Cookie", "JSESSIONID=" + getSessionId()).
                when().
                delete("/rest/api/2/filter/" + filterID + "/favourite").
                then().
                statusCode(200).
                log().status().
                log().body();
    }

    public void getFilterPermission(String filterID, String permissionID) {
        System.out.println("-----------------------------GETTING FILTER PERMISSION ID-----------------------------");
        RestAssured.baseURI = "http://jira.hillel.it:8080"; // rest assured preconfiguration
        given().
                header("Content-Type", "application/json").
                header("Cookie", "JSESSIONID=" + getSessionId()).
                when().
                get("/rest/api/2/filter/" + filterID + "/permission/" + permissionID).
                then().
                statusCode(200).
                log().status().
                log().body();
    }

    public String addFilterPermissionAndGetID(String filterID, String requestBody) {
        System.out.println("-----------------------------ADDING FILTER PERMISSION-----------------------------");
        RestAssured.baseURI = "http://jira.hillel.it:8080"; // rest assured preconfiguration
        String newPermissionID= given().
            header("Content-Type", "application/json").
            header("Cookie", "JSESSIONID=" + getSessionId()).
            body(requestBody).
            when().
            post("/rest/api/2/filter/" + filterID + "/permission").
            then().
            statusCode(201).
            log().status().
            log().body().
            extract().path("id").toString().replace("[", "").replace("]", "");
        return newPermissionID;
    }

    public void deleteFilterPermission(String filterID, String permissionID) {
        System.out.println("-----------------------------DELETING FILTER PERMISSION ID BY ID-----------------------------");
        RestAssured.baseURI = "http://jira.hillel.it:8080"; // rest assured preconfiguration
        given().
                header("Content-Type", "application/json").
                header("Cookie", "JSESSIONID=" + getSessionId()).
                when().
                delete("/rest/api/2/filter/" + filterID + "/permission/" + permissionID).
                then().
                statusCode(204).
                log().status().
                log().body();
    }

    public void getAllFilterPermissions(String filterID) {
        System.out.println("-----------------------------GETTING FILTER PERMISSIONS-----------------------------");
        RestAssured.baseURI = "http://jira.hillel.it:8080"; // rest assured preconfiguration
        given().
            header("Content-Type", "application/json").
            header("Cookie", "JSESSIONID=" + getSessionId()).
            when().
            get("/rest/api/2/filter/" + filterID + "/permission").
            then().
            statusCode(200).
            log().status().
            log().body();
    }

    @Test(groups = "CRITICAL, HTTP")
    public void loginToJira() {
        RestAssured.baseURI = "http://jira.hillel.it:8080"; // rest assured preconfiguration
        given().
            header("Content-Type", "application/json").
            body(credentialsJSON).
            when().
            post("/rest/auth/1/session").
            then().statusCode(200);
    }

    @Test(groups = "Regression, HTTP", dependsOnGroups = "CRITICAL, HTTP")
    public void createFilter() {
        String newFilterID = createFilterAndGetID(filterJSON);
        getFilter(newFilterID);
        deleteFilter(newFilterID);
    }

    @Test(groups = "Regression, HTTP", dependsOnGroups = "CRITICAL, HTTP")
    public void updateFilterName() {
        // update filter name
        updateFilter(newNameJSON, testFilterID);
        // update filter name to its initial value
        updateFilter(initialNameJSON, testFilterID);
    }

    @Test(groups = "Regression, HTTP", dependsOnGroups = "CRITICAL, HTTP")
    public void updateFilterJQL() {
        // update filter jql
        updateFilter(newJQL, testFilterID);
        // update filter jql to its initial value
        updateFilter(initialJQL, testFilterID);
    }

    // TODO currently this test always returns code 404, need to fix it(don't know how)
    @Test(groups = "Regression, HTTP", dependsOnGroups = "CRITICAL, HTTP")
    public void addFilterToFavourite() {
        setFavouriteFlag(testFilterID);
        deleteFavouriteFlag(testFilterID);
    }

    @Test(groups = "Regression, HTTP", dependsOnGroups = "CRITICAL, HTTP")
    public void setFilterPermissions() {
        String newPermissionID = addFilterPermissionAndGetID(testFilterID, newPermission);
        getFilterPermission(testFilterID, newPermissionID);
        deleteFilterPermission(testFilterID, newPermissionID);
    };
}

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

public class FilterTest {

    private String credentialsJSON = "{" +
            "\"username\": \"qa_auto_5_team_2\"," +
            "\"password\": \"qa_auto_5_team_2\"" +
            "} ";
    private final String testFilterID = "10904";
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
    public void updateFilterName() {
        RestAssured.baseURI = "http://jira.hillel.it:8080"; // rest assured preconfiguration
        String initialNameJSON = "{" +
                "\"name\": \"Alesya test filter\"" +
                "} ";
        String newNameJSON = "{" +
                "\"name\": \"Alesya test filter UPDATED\"" +
                "} ";
        System.out.println("-----------------------------Update filter name-----------------------------");
        // Update filter name
        given().
            header("Content-Type", "application/json").
            header("Cookie", "JSESSIONID=" + getSessionId()).
            body(newNameJSON).
            when().
            put("/rest/api/2/filter/" + testFilterID).
            then().
            statusCode(200).
                log().status().
                log().body();

        System.out.println("-----------------------------Update filter name to its initial value-----------------------------");
        // Update filter name to its initial value
        given().
                header("Content-Type", "application/json").
                header("Cookie", "JSESSIONID=" + getSessionId()).
                body(initialNameJSON).
                when().
                put("/rest/api/2/filter/" + testFilterID).
                then().
                statusCode(200).
                    log().status().
                    log().body();
    }

    @Test(groups = "Regression, HTTP", dependsOnGroups = "CRITICAL, HTTP")
    public void updateFilterJQL() {
        RestAssured.baseURI = "http://jira.hillel.it:8080"; // rest assured preconfiguration
        String initialJQL = "{" +
                "\"jql\": \"project = QAAUT AND issuetype = Story AND status = Backlog AND priority = Medium\"" +
                "} ";
        String newJQL = "{" +
                "\"jql\": \"project = QAAUT AND issuetype = Bug AND status = Backlog\"" +
                "} ";
        System.out.println("-----------------------------Update filter jql-----------------------------");
        // Update filter jql
        given().
                header("Content-Type", "application/json").
                header("Cookie", "JSESSIONID=" + getSessionId()).
                body(newJQL).
                when().
                put("/rest/api/2/filter/" + testFilterID).
                then().
                statusCode(200).
                    log().status().
                    log().body();

        System.out.println("-----------------------------Update jql to its initial value-----------------------------");
        // Update jql to its initial value
        given().
                header("Content-Type", "application/json").
                header("Cookie", "JSESSIONID=" + getSessionId()).
                body(initialJQL).
                when().
                put("/rest/api/2/filter/" + testFilterID).
                then().
                statusCode(200).
                    log().status().
                    log().body();
    }

    @Test(groups = "Regression, HTTP", dependsOnGroups = "CRITICAL, HTTP")
    public void addFilterToFavourite() {
        RestAssured.baseURI = "http://jira.hillel.it:8080"; // rest assured preconfiguration
        System.out.println("-----------------------------add filter to favourite-----------------------------");
        // add filter to favourite
        given().
                header("Content-Type", "application/json").
                header("Cookie", "JSESSIONID=" + getSessionId()).
                when().
                put("/rest/api/2/filter/" + testFilterID + "/favourite").
                then().
                statusCode(200).
                    log().status().
                    log().body();

        System.out.println("-----------------------------remove filter from favourite-----------------------------");
        // remove filter from favourite
        given().
                header("Content-Type", "application/json").
                header("Cookie", "JSESSIONID=" + getSessionId()).
                when().
                put("/rest/api/2/filter/" + testFilterID + "/favourite").
                then().
                statusCode(200).
                    log().status().
                    log().body();
    }
}

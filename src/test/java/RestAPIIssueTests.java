import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class RestAPIIssueTests {
    String sessionId = "";
    String projectId = "10502";
    String issueType = "13561";

    @Test(groups = {"CRITICAL", "HTTP"})
    public void authentication(){
        RestAssured.baseURI = "http://jira.hillel.it:8080";
        String credentialsJSON = "{" +
                "\"username\": \"qa_auto_5_team_1\"," +
                "\"password\": \"qa_auto_5_team_1\"" +
                "} "
                ;

        /* HTTP Requests for login*/
        sessionId = given().
            header("Content-Type", "application/json").
            body(credentialsJSON).
            when().
            post("/rest/auth/1/session").
            then().
            extract().
            path("session.value");

        System.out.printf("\nSESSION: "+ sessionId);
}


    @Test(groups = {"Regression, HTTP"},dependsOnGroups = {"CRITICAL"})
    public void commentCRUD(){
        RestAssured.baseURI = "http://jira.hillel.it:8080";
        ValidatableResponse response;

         /* test data and parameters */


        String jsonForUpdateComment = "";
        String myComment= "test";
        String jsonForAddComment = "{\"body\" : \"" + myComment + "\"}";

        /* HTTP Requests for addComment*/
        response = given().
                header("Content-Type", "application/json").
                body(jsonForAddComment).
                when().
                post("/rest/api/2/issue/13561/comment").
                then();


        String myCommentFromServer =response.extract().path("body");
        String wholeJSON =response.extract().asString();
        response.log().all();
        response.statusCode(201);
        response.contentType(ContentType.JSON);

        assertEquals(myComment,myCommentFromServer );



    }

    @Test(groups = {"Regression, HTTP"},dependsOnGroups = {"CRITICAL"})
    public void descriptionCRUD(){
        RestAssured.baseURI = "http://jira.hillel.it:8080";
        ValidatableResponse response;
        
        String jsonForAddDescription = "{\"fields\":{" + "\"description\": \"My description\""+"}}";
        response = given().
                header("Content-Type", "application/json").
                header("Cookie", "JSESSIONID=" + sessionId).
                body(jsonForAddDescription).
                when().
                put("/rest/api/2/issue/13561").
                then().log().all().
                statusCode(204).contentType(ContentType.JSON);

        String responseBody = response.extract().asString();
        System.out.printf("\nRESPONSE: " + responseBody);

        String jsonForDeleteDescription = "{\"fields\":{" + "\"description\": \"\""+"}}";
        response = given().
                header("Content-Type", "application/json").
                header("Cookie", "JSESSIONID=" + sessionId).
                body(jsonForDeleteDescription).
                when().
                put("/rest/api/2/issue/13561").
                then().log().all().
                statusCode(204).contentType(ContentType.JSON);

        System.out.printf("\nRESPONSE: " + responseBody);
    }

    @Test(groups = {"Regression, HTTP"},dependsOnGroups = {"CRITICAL"})
    public void remoteIssueLinksCRUD() {
        RestAssured.baseURI = "http://jira.hillel.it:8080";
        ValidatableResponse response;
        String jsonForAddRemoteLink = "{\"object\": {\n" +
                "\"url\": \"https://obmenka.od.ua/\",\n" +
                "\"title\": \"obmenka\"\n" +
                "}}";

        /* HTTP Request to get ID's for Remote Issue Links*/
        String result = given().
                header("Accept","application/json").
                header("Cookie", "JSESSIONID=" + sessionId).
                when().
                get("/rest/api/2/issue/13561/remotelink").
                then().
                log().all().
                extract().
                asString();

        /* create new link to Remote Issue */
        response = given().
                header("Accept","application/json").
                header("Content-Type","application/json").
                header("Cookie", "JSESSIONID=" + sessionId).
                body(jsonForAddRemoteLink).
                when().
                post("/rest/api/2/issue/13561/remotelink").
                then().log().all().
                statusCode(201).contentType(ContentType.JSON);
        String responseBody = response.extract().asString();
        System.out.printf("\nRESPONSE: " + responseBody);
        String linkId = response.extract().path("id").toString();
        
        /* delete link to Remote Issue */
        given().
                header("Content-Type", "application/json").
                header("Cookie", "JSESSIONID=" + sessionId).
                body(jsonForAddRemoteLink).
                when().
                delete("/rest/api/2/issue/" + issueType +"/remotelink/"+ linkId).
                then().log().all().
                statusCode(204).contentType(ContentType.JSON);

    }
}

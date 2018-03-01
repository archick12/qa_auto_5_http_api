package utils.api;

import io.restassured.RestAssured;
import org.apache.log4j.Logger;
import io.restassured.http.ContentType;
import utils.data.JiraJsonObjectHelper;
import utils.framework.HTTPTestsListener;

import static io.restassured.RestAssured.given;

public class Authorization {

    public static String JSESSIONID;
    public static String BASE_URI = "http://jira.hillel.it:8080";
    public static String username = HTTPTestsListener.propertiesJira.get("username");
    public static String password = HTTPTestsListener.propertiesJira.get("password");
    static final Logger logger = Logger.getLogger(Authorization.class);


    public static void loginToJIRA() {
        RestAssured.baseURI = BASE_URI;

        String loginJson = JiraJsonObjectHelper.generateJSONForLogin();
        JSESSIONID =
                given().
                        header("Content-Type", ContentType.JSON).
                        body(loginJson).
                        when().
                        post(APIPathes.login).
                        then().
                        statusCode(200).contentType(ContentType.JSON).
//                        log().all().
                        extract().
                        path("session.value");

        logger.info("\nAUTHORIZATION TOKEN: " + Authorization.JSESSIONID);
    }
}
package utils.data;

import org.json.simple.JSONObject;
import utils.api.Authorization;

import java.util.HashMap;

public class JiraJsonObjectHelper {


  public static String generateJSONForLogin() {
    JSONObject credentials = new JSONObject();
    credentials.put("username", Authorization.username);
    credentials.put("password", Authorization.password);

    return credentials.toJSONString();
  }

  public static String generateJSONForIssue(String projectId, String summary, String issueType,
                                            String assignee) {

    JSONObject issueData = new JSONObject();
    JSONObject fieldsJSONObject = new JSONObject();
    JSONObject projectJSONObject = new JSONObject();
    JSONObject issuetypeJSONObject = new JSONObject();
    JSONObject assigneeJSONObject = new JSONObject();

    projectJSONObject.put("id", projectId);
    issuetypeJSONObject.put("id", issueType);
    assigneeJSONObject.put("name", assignee);

    fieldsJSONObject.put("project", projectJSONObject);
    fieldsJSONObject.put("summary", summary);
    fieldsJSONObject.put("issuetype", issuetypeJSONObject);
    fieldsJSONObject.put("assignee", assigneeJSONObject);

    issueData.put("fields", fieldsJSONObject);

    return issueData.toString();
  }

  public static String generateJSONForComment(String comment) {
    JSONObject commentJSON = new JSONObject();
    commentJSON.put("body", comment);

    return commentJSON.toJSONString();
  }

  public static String generateJSONForDescription(String description) {
    JSONObject descriptionJSON = new JSONObject();
    JSONObject descriptionValue = new JSONObject();

    descriptionValue.put("description", description);
    descriptionJSON.put("fields", descriptionValue);

    return descriptionJSON.toJSONString();
  }

  public static String generateJSONForRemoteLink(String url, String title) {
    JSONObject remoteLinkJSON = new JSONObject();
    JSONObject objectJSON = new JSONObject();

    objectJSON.put("url", url);
    objectJSON.put("title", title);

    remoteLinkJSON.put("relationship", "blocks");
    remoteLinkJSON.put("object", objectJSON);
    return remoteLinkJSON.toJSONString();
  }
}


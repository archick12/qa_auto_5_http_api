package utils.data;

import org.json.simple.JSONObject;
import utils.api.Authorization;
import java.util.ArrayList;

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
    fieldsJSONObject.put("issuetype", issuetypeJSONObject);
    fieldsJSONObject.put("summary", summary);
    fieldsJSONObject.put("assignee", assigneeJSONObject);

    issueData.put("fields", fieldsJSONObject);

    return issueData.toString();
  }

  public static String generateJSONForComment(String comment) {
    JSONObject commentJSON = new JSONObject();
    commentJSON.put("body", comment);

    return commentJSON.toJSONString();
  }

  public static String generateJSONForSearch(String searchString) {
    JSONObject searchJSON = new JSONObject();
    ArrayList<String> fieldsList = new ArrayList<String>();
    fieldsList.add("project");
    fieldsList.add("issuetype");
    fieldsList.add("assignee");
    fieldsList.add("status");
    fieldsList.add("summary");

    searchJSON.put("jql", searchString);
    searchJSON.put("startAt", 0);
    searchJSON.put("maxResults", 50);
    searchJSON.put("fields",fieldsList);

    return searchJSON.toJSONString();
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

  public static String generateJSONForNewFilter(String filterName, String filterDescription, String filterJQL, boolean filterFavouriteFlag) {
    JSONObject newFilter = new JSONObject();
    newFilter.put("name", filterName);
    newFilter.put("description", filterDescription);
    newFilter.put("jql", filterJQL);
    newFilter.put("favourite", filterFavouriteFlag);
    return newFilter.toJSONString();
  }

  public static String generateJSONForFilterName(String filterName) {
    JSONObject name = new JSONObject();
    name.put("name", filterName);
    return name.toJSONString();
  }

  public static String generateJSONForFilterJQL(String filterJQL) {
    JSONObject jql = new JSONObject();
    jql.put("jql", filterJQL);
    return jql.toJSONString();
  }

  public static String generateJSONForFilterPermission(String type, String groupname) {
    JSONObject permission = new JSONObject();
    permission.put("type", type);
    permission.put("groupname", groupname);
    return permission.toJSONString();
  }

  public static String generateJSONForIssueTransition(String transitionID) {
    JSONObject transition = new JSONObject();
    JSONObject transitionData = new JSONObject();
    transitionData.put("id", transitionID);
    transition.put("transition", transitionData);
    return transition.toJSONString();
  }
}



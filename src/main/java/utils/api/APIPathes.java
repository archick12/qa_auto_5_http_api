package utils.api;

public interface APIPathes {

  String login = "/rest/auth/1/session/";
  String issue = "/rest/api/2/issue/";
  String commentInIssue = "/rest/api/2/issue/%s/comment";
  String existingCommentInIssue = "/rest/api/2/issue/%s/comment/%s";
  String searchIssues = "/rest/api/2/search";

}
package utils.api;

public interface APIPathes {

  String login = "/rest/auth/1/session/";
  String issue = "/rest/api/2/issue/";
  String commentInIssue = "/rest/api/2/issue/%s/comment";
  String existingCommentInIssue = "/rest/api/2/issue/%s/comment/%s";
  String descriptionInIssue = "/rest/api/2/issue/%s";
  String remoteIssueLink = "/rest/api/2/issue/%s/remotelink";
  String existingRemoteIssueLink = "/rest/api/2/issue/%s/remotelink/%s";

}
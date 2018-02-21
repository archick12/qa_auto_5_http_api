package utils.api;

public interface APIPathes {

  String login = "/rest/auth/1/session/";
  String issue = "/rest/api/2/issue/";
  String commentInIssue = "/rest/api/2/issue/%s/comment";
  String existingCommentInIssue = "/rest/api/2/issue/%s/comment/%s";
  String filter = "/rest/api/2/filter/";
  String existingFilter = "/rest/api/2/filter/%s";
}
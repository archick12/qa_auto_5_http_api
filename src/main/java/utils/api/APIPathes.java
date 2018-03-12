package utils.api;

public interface APIPathes {

  String login = "/rest/auth/1/session/";
  String issue = "/rest/api/2/issue/";
  String commentInIssue = "/rest/api/2/issue/%s/comment";
  String existingCommentInIssue = "/rest/api/2/issue/%s/comment/%s";
  String searchIssues = "/rest/api/2/search";
  String descriptionInIssue = "/rest/api/2/issue/%s";
  String remoteIssueLink = "/rest/api/2/issue/%s/remotelink";
  String existingRemoteIssueLink = "/rest/api/2/issue/%s/remotelink/%s";
  String filter = "/rest/api/2/filter/";
  String existingFilter = "/rest/api/2/filter/%s";
  String filterFavourite = "/rest/api/2/filter/%s/favourite/";
  String filterAllPermissions = "/rest/api/2/filter/%s/permission/";
  String filterPermission = "/rest/api/2/filter/%s/permission/%s/";
  String issueTransitions = "/rest/api/2/issue/%s/transitions";

}
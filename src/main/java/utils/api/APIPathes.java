package utils.api;

public interface APIPathes {

  String login = "/rest/auth/1/session/";
  String issue = "/rest/api/2/issue/";
  String commentInIssue = "/rest/api/2/issue/%s/comment";
  String existingCommentInIssue = "/rest/api/2/issue/%s/comment/%s";
  String addDescriptionInIssue = "/rest/api/2/issue/%s";
  String existingDescriptionInIssue = "{\"fields\":{" + "\"description\": \"\"" + "}}";
  String addRemoteLink = "\"/rest/api/2/issue/13561/remotelink\"";
  String deleteRemoteLinkInIssue = "\"/rest/api/2/issue/\" + issueId + \"/remotelink/\" + linkId";
}
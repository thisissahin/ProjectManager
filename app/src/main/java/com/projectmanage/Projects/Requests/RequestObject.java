package com.projectmanage.Projects.Requests;

public class RequestObject {

    String projectKey;
    String requestUser;
    String projectName;

    public RequestObject(String projectKey,String requestUser,String projectName) {
        this.projectKey = projectKey;
        this.requestUser = requestUser;
        this.projectName = projectName;
    }




    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }
    public String getRequestUser() {
        return requestUser;
    }

    public void setRequestUser(String requestUser) {
        this.requestUser = requestUser;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
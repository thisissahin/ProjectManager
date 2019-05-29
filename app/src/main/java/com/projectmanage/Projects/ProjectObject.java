package com.projectmanage.Projects;

public class ProjectObject {

    String projectKey;
    String projectName;

    public ProjectObject(String projectKey,String projectName) {
        this.projectKey = projectKey;
        this.projectName = projectName;
    }




    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}

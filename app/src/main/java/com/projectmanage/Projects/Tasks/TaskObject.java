package com.projectmanage.Projects.Tasks;

public class TaskObject {
    private String title;
    private String taskKey;
    private String projectKey;
    private String date;
    private String projectName;



    public TaskObject(String title, String taskKey, String date, String projectKey, String projectName) {
        this.title = title;
        this.taskKey = taskKey;
        this.date = date;
        this.projectKey = projectKey;
        this.projectName = projectName;
    }
    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTaskKey() {
        return taskKey;
    }

    public void setTaskKey(String taskKey) {
        this.taskKey = taskKey;
    }
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}

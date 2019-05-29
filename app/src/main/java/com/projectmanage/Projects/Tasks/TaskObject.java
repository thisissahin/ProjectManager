package com.projectmanage.Projects.Tasks;

public class TaskObject {
    private String task;
    private String taskKey;
    private String projectKey;
    private String date;
    private String projectName;



    public TaskObject(String task, String taskKey, String date, String projectKey, String projectName) {
        this.task = task;
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

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
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

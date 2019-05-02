package com.projectmanage.Projects.Requests;

public class RequestObject {

    String requestKey;
    String requestUser;

    public RequestObject(String requestKey,String requestUser) {
        this.requestKey = requestKey;
        this.requestUser = requestUser;
    }




    public String getRequestKey() {
        return requestKey;
    }

    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey;
    }
    public String getRequestUser() {
        return requestUser;
    }

    public void setRequestUser(String requestUser) {
        this.requestUser = requestUser;
    }
}
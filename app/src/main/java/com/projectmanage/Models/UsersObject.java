package com.projectmanage.Models;

public class UsersObject {
    String userName;
    String userKey;
    String userStatus;

    public UsersObject(String userName, String userKey, String userStatus) {
        this.userName = userName;
        this.userKey = userKey;
        this.userStatus = userStatus;
    }



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
}

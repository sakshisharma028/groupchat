package com.example.lavisha.project;

public class User {
    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String id;
    String dp;
    String username;
    String status;

    public User(String id, String dp, String username, String status) {
        this.id = id;
        this.dp = dp;
        this.username = username;
        this.status = status;
    }
}

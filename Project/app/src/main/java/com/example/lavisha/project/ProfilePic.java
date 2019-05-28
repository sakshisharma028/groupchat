package com.example.lavisha.project;

public class ProfilePic {

    String urlOfImage;
    String username;


    public String getUrlOfImage() {
        return urlOfImage;
    }

    public void setUrlOfImage(String urlOfImage) {
        this.urlOfImage = urlOfImage;
    }

    public ProfilePic(String urlOfImage, String username) {
        this.urlOfImage = urlOfImage;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

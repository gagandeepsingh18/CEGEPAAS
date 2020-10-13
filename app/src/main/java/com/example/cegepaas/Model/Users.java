package com.example.cegepaas.Model;

public class Users {
    private String name;
    private String email;
    private String password;
    private String username;
    private String downloadImageUrl;

    public Users() {
    }

    public Users(String name, String email, String password, String username, String downloadImageUrl) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.username = username;
        this.downloadImageUrl = downloadImageUrl;
    }

    public String getDownloadImageUrl() {
        return downloadImageUrl;
    }

    public void setDownloadImageUrl(String downloadImageUrl) {
        this.downloadImageUrl = downloadImageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

package com.example.cegepaas.Model;

public class AdvisorsPojo {
    private String username;
    private String email;
    private String name;
    private String image;
    private String password;
    private String status;

    public AdvisorsPojo() {

    }

    public AdvisorsPojo(String username, String email, String name, String image, String password, String status) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.image = image;
        this.password = password;
        this.status = status;
    }

    public AdvisorsPojo(String username, String email, String name, String password, String status) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.password = password;
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

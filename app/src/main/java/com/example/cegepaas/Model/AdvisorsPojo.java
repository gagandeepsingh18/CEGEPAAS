package com.example.cegepaas.Model;

public class AdvisorsPojo {
    private String username;
    private String email;
    private String name;
    private String image;
    private String password;
    private String status;
    private String phoneNumber;
    private String campus;
    private String department;
    private String workingDays;
    private String description;

    public AdvisorsPojo() {

    }

    public AdvisorsPojo(String username, String email, String name, String image, String phoneNumber, String campus, String department, String workingDays, String description) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.image = image;
        this.phoneNumber = phoneNumber;
        this.campus = campus;
        this.department = department;
        this.workingDays = workingDays;
        this.description = description;
    }
    public AdvisorsPojo(String username, String email, String name, String image, String password, String status, String phoneNumber, String campus, String department, String workingDays, String description) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.image = image;
        this.password = password;
        this.status = status;
        this.phoneNumber = phoneNumber;
        this.campus = campus;
        this.department = department;
        this.workingDays = workingDays;
        this.description = description;
    }

    public AdvisorsPojo(String username, String email, String name, String password, String status) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.password = password;
        this.status = status;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(String workingDays) {
        this.workingDays = workingDays;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

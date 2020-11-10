package com.example.cegepaas.Model;

/**
 * Advisors POJO Class
 */
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

    /**
     * Non-parameterized constructor
     */
    public AdvisorsPojo() {

    }

    /**
     * Parameterized constructor
     * @param username : Advisor Id
     * @param email : email
     * @param name : name
     * @param image : image URL
     * @param phoneNumber : phone Number
     * @param campus : campus
     * @param department : department
     * @param workingDays : working Days
     * @param description : description
     */
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

    /**
     * Parameterized constructor
     * @param username : Advisor Id
     * @param email: email
     * @param name: name
     * @param image: image URL
     * @param password : password
     * @param status : status
     * @param phoneNumber: phone Number
     * @param campus: campus
     * @param department: department
     * @param workingDays: working Days
     * @param description: description
     */
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

    /**
     * get Phone
     * @return : phone
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * get Campus
     * @return : campus
     */
    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    /**
     * get Department
     * @return : department
     */
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * get Working Days
     * @return : working Days
     */
    public String getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(String workingDays) {
        this.workingDays = workingDays;
    }

    /**
     * get Description
     * @return : description
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * get Image URL
     * @return : image URL
     */
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    /**
     * get Status
     * @return : status
     */
    public String getStatus() {
        return status;
    }

    /**
     * set Status
     * @param status : status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * get Advisor Id
     * @return : advisor Id
     */
    public String getUsername() {
        return username;
    }

    /**
     * set Advisor Name
     * @param username : userName
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * get Email
     * @return : email
     */
    public String getEmail() {
        return email;
    }

    /**
     * set Email
     * @param email: email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * get Name
     * @return : name
     */
    public String getName() {
        return name;
    }

    /**
     * set Name
     * @param name : name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get Password
     * @return : password
     */
    public String getPassword() {
        return password;
    }

    /**
     * set Password
     * @param password : password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}

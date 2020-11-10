package com.example.cegepaas.Model;

/**
 * Users Class
 */
public class Users {
    private String name;
    private String email;
    private String password;
    private String username;
    private String downloadImageUrl;

    public Users() {
    }

    /**
     * Parameterized constructor
     * @param name : name
     * @param email : email
     * @param password : password
     * @param username : userName
     * @param downloadImageUrl : image URL
     */
    public Users(String name, String email, String password, String username, String downloadImageUrl) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.username = username;
        this.downloadImageUrl = downloadImageUrl;
    }

    /**
     * get Image URL
     * @return : image Url
     */
    public String getDownloadImageUrl() {
        return downloadImageUrl;
    }

    public void setDownloadImageUrl(String downloadImageUrl) {
        this.downloadImageUrl = downloadImageUrl;
    }

    /**
     * get Name
     * @return : name
     */
    public String getName() {
        return name;
    }

    /**
     * set name
     * @param name : name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get Email
     * @return : email
     */
    public String getEmail() {
        return email;
    }

    /**
     * set email
     * @param email : email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * get Password
     * @return : password
     */
    public String getPassword() {
        return password;
    }

    /**
     * set password
     * @param password : password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * get User Name
     * @return : userName
     */
    public String getUsername() {
        return username;
    }

    /**
     * set UserName
     * @param username : userName
     */
    public void setUsername(String username) {
        this.username = username;
    }
}

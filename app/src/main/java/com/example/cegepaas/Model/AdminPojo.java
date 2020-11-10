package com.example.cegepaas.Model;

/**
 * Admin POJO Class
 */
public class AdminPojo {
    /**
     * get UserName
     * @return : username
     */
    public String getUname() {
        return uname;
    }

    /**
     * set userName
     * @param uname : setusername
     */
    public void setUname(String uname) {
        this.uname = uname;
    }

    /**
     * get password
     * @return : password
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * set password
     * @param pwd  :password
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     * constructor Admin
     * @param uname : userName
     * @param pwd : password
     */
    public AdminPojo(String uname, String pwd) {
        this.uname = uname;
        this.pwd = pwd;
    }

    private String uname;
    private String pwd;

    /**
     * no-parameter constructor
     */
    public AdminPojo() {

    }
}

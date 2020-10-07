package com.example.cegepaas.Model;

public class  AdminPojo {
    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public AdminPojo(String uname, String pwd) {
        this.uname = uname;
        this.pwd = pwd;
    }

    private String uname;
    private String pwd;

    public AdminPojo()
    {

    }
}

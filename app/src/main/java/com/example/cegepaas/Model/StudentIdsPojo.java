package com.example.cegepaas.Model;

/**
 * StudentIds POJO
 */
public class StudentIdsPojo {
    private String sid;

    public StudentIdsPojo(String sid) {
        this.sid = sid;
    }

    public StudentIdsPojo() {
    }

    /**
     * get StudentID
     * @return : studentId
     */
    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
}

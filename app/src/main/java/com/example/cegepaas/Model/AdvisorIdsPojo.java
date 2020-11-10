package com.example.cegepaas.Model;

/**
 * AdvisorIds POJO
 */
public class AdvisorIdsPojo {
    private String aid;

    /**
     * parameterized constructor
     * @param aid : advisor Id
     */
    public AdvisorIdsPojo(String aid) {
        this.aid = aid;
    }

    /**
     * non-parameterized constructor
     */
    public AdvisorIdsPojo() {
    }

    /**
     * get Advisor Id
     * @return : advisor Id
     */
    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }
}

package com.example.cegepaas.Model;

import com.google.gson.annotations.SerializedName;

public class AdvisorHomePOJO {
    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSchedule_time() {
        return schedule_time;
    }

    public void setSchedule_time(String schedule_time) {
        this.schedule_time = schedule_time;
    }

    public AdvisorHomePOJO(String name, String email, String phone, String schedule_time) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.schedule_time = schedule_time;
    }

    @SerializedName("schedule_time")
    private String schedule_time;

}

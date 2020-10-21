package com.example.cegepaas.Model;

public class AvailableTimings {
    private String booked_time;
    private String booked_status;

    public AvailableTimings() {
    }

    public AvailableTimings(String booked_time, String booked_status) {
        this.booked_time = booked_time;
        this.booked_status = booked_status;
    }

    public String getBooked_time() {
        return booked_time;
    }

    public void setBooked_time(String booked_time) {
        this.booked_time = booked_time;
    }

    public String Get_status() {
        return booked_status;
    }

    public void Set_status(String booked_status) {
        this.booked_status = booked_status;
    }
}

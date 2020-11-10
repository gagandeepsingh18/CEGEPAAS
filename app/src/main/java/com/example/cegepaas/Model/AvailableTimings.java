package com.example.cegepaas.Model;

/**
 * Available Timings POJO
 */
public class AvailableTimings {
    private String booked_time;
    private String status;

    public AvailableTimings() {
    }

    public AvailableTimings(String booked_time, String status) {
        this.booked_time = booked_time;
        this.status = status;
    }

    /**
     * get Booked time
     * @return : booked Time
     */
    public String getBooked_time() {
        return booked_time;
    }

    public void setBooked_time(String booked_time) {
        this.booked_time = booked_time;
    }

    /**
     * get Status
     * @return : Status
     */
    public String getStatus() {
        return status;
    }

    public void getStatus(String booked_status) {
        this.status = booked_status;
    }
}

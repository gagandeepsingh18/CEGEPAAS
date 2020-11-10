package com.example.cegepaas.Model;

/**
 * Advisor Booking Pojo class
 */
public class AdvisorBookingPojo {
    private String adv_username;
    private String booked_by;
    private String booked_date;
    private String booked_time;
    private String created_at;
    private String description;
    private String status;
    private String timestamp;

    /**
     * Non- parameterized constructor
     */
    public AdvisorBookingPojo() {

    }

    /**
     * parameterized constructor
     * @param adv_username : advisor Id
     * @param booked_by : Student Id
     * @param booked_date : booking date
     * @param booked_time : booked time
     * @param created_at : created date
     * @param description : meeting description
     * @param status : status
     * @param timestamp : timeStamp
     */
    public AdvisorBookingPojo(String adv_username, String booked_by, String booked_date, String booked_time, String created_at, String description, String status, String timestamp) {
        this.adv_username = adv_username;
        this.booked_by = booked_by;
        this.booked_date = booked_date;
        this.booked_time = booked_time;
        this.created_at = created_at;
        this.description = description;
        this.status = status;
        this.timestamp = timestamp;
    }

    /**
     * get Advisor Id
     * @return : advisor ID
     */
    public String getAdv_username() {
        return adv_username;
    }


    public void setAdv_username(String adv_username) {
        this.adv_username = adv_username;
    }

    /**
     * get student Id
     * @return : student Id
     */
    public String getBooked_by() {
        return booked_by;
    }

    public void setBooked_by(String booked_by) {
        this.booked_by = booked_by;
    }

    /**
     * get booking date
     * @return : booking date
     */
    public String getBooked_date() {
        return booked_date;
    }

    public void setBooked_date(String booked_date) {
        this.booked_date = booked_date;
    }

    /**
     * get booking time
     * @return : booking time
     */
    public String getBooked_time() {
        return booked_time;
    }

    public void setBooked_time(String booked_time) {
        this.booked_time = booked_time;
    }

    /**
     * get created date
     * @return : created date
     */
    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
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
     * get timestamp
     * @return : timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

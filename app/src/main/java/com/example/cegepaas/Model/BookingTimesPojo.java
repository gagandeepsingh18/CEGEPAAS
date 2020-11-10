package com.example.cegepaas.Model;

/**
 * Booking Times POJO
 */
public class BookingTimesPojo {
    private String time;
    private String available;

    /**
     * Parameterized constructor
     * @param time : time
     * @param available : available
     */
    public BookingTimesPojo(String time, String available) {
        this.time = time;
        this.available = available;
    }

    /**
     * get time
     * @return : time
     */
    public String getTime() {
        return time;
    }

    /**
     * set time
     * @param time : time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * get Available
     * @return : available
     */
    public String getAvailable() {
        return available;
    }

    /**
     * set Availability
     * @param available : available
     */
    public void setAvailable(String available) {
        this.available = available;
    }
}

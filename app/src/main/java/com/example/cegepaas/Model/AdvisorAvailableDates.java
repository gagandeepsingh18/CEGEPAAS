package com.example.cegepaas.Model;

/**
 * Advisor Available Dates POJO Class
 */
public class AdvisorAvailableDates {
    private String adv_username;
    private String booking_date;
    private String booking_times;

    /**
     * non parameter constructor
     */
    public AdvisorAvailableDates() {
    }

    /**
     * Parameterized Constructor
     * @param adv_username : advisor username
     * @param booking_date : booking date
     * @param booking_times : booking time
     */
    public AdvisorAvailableDates(String adv_username, String booking_date, String booking_times) {
        this.adv_username = adv_username;
        this.booking_date = booking_date;
        this.booking_times = booking_times;
    }

    /**
     * get advisor userName
     * @return : advisor userName
     */
    public String getAdv_username() {
        return adv_username;
    }

    /**
     * set advisor userName
     * @param adv_username : advisor userName
     */
    public void setAdv_username(String adv_username) {
        this.adv_username = adv_username;
    }

    /**
     * get booking Date
     * @return : booking date
     */
    public String getBooking_date() {
        return booking_date;
    }

    /**
     * set Booking date
     * @param booking_date : booking date
     */
    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    /**
     * get booking times
     * @return : booking times
     */
    public String getBooking_times() {
        return booking_times;
    }

    /**
     * set booking times
     * @param booking_times :booking times
     */
    public void setBooking_times(String booking_times) {
        this.booking_times = booking_times;
    }
}

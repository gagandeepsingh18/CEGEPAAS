package com.example.cegepaas.Model;

public class AdvisorAvailableDates {
    private String adv_username;
    private String booking_date;
    private String booking_times;
    public AdvisorAvailableDates() {
    }

    public AdvisorAvailableDates(String adv_username, String booking_date, String booking_times) {
        this.adv_username = adv_username;
        this.booking_date = booking_date;
        this.booking_times = booking_times;
    }

    public String getAdv_username() {
        return adv_username;
    }

    public void setAdv_username(String adv_username) {
        this.adv_username = adv_username;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getBooking_times() {
        return booking_times;
    }

    public void setBooking_times(String booking_times) {
        this.booking_times = booking_times;
    }
}

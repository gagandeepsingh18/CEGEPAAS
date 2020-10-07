package com.example.cegepaas.Model;

public class BookingTimesPojo {
    private String time;
    private String available;

    public BookingTimesPojo(String time, String available) {
        this.time = time;
        this.available = available;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }
}

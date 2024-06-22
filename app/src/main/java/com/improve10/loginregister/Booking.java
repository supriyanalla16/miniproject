package com.improve10.loginregister;

public class Booking {
    private String consultantName;
    private String timeSlot;

    // Default constructor required for calls to DataSnapshot.getValue(Booking.class)
    public Booking() {
    }

    public Booking(String consultantName, String timeSlot) {
        this.consultantName = consultantName;
        this.timeSlot = timeSlot;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }
}
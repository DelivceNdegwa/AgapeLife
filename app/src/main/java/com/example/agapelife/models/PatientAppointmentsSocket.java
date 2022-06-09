package com.example.agapelife.models;

// List of all patient appointments
public class PatientAppointmentsSocket {
    long id;
    String title, about;
    boolean status;

    public PatientAppointmentsSocket() {
    }

    public PatientAppointmentsSocket(long id, String title, String about, boolean status) {
        this.id = id;
        this.title = title;
        this.about = about;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}

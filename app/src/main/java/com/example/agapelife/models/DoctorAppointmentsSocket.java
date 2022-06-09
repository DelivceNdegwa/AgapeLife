package com.example.agapelife.models;

// For appointment lists
public class DoctorAppointmentsSocket {
    long id, clientId, doctorId;
    int status;
    String about;
    boolean read;

    public DoctorAppointmentsSocket() {
    }

    public DoctorAppointmentsSocket(long id, long clientId, long doctorId, int status, String about, boolean read) {
        this.id = id;
        this.clientId = clientId;
        this.doctorId = doctorId;
        this.status = status;
        this.about = about;
        this.read = read;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(long doctorId) {
        this.doctorId = doctorId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}

package com.example.agapelife.models;

public class Appointment {
    String appointmentTitle, startTime, endTime;
    long clientId, patientId;

    public Appointment() {
    }

    public Appointment(String appointmentTitle, String startTime, String endTime, long clientId, long patientId) {
        this.appointmentTitle = appointmentTitle;
        this.startTime = startTime;
        this.endTime = endTime;
        this.clientId = clientId;
        this.patientId = patientId;
    }

    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    public void setAppointmentTitle(String appointmentTitle) {
        this.appointmentTitle = appointmentTitle;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }
}

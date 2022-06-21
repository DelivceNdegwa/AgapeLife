package com.example.agapelife.models;

public class Appointment {
    String appointmentTitle, appointmentAbout, startTime, endTime;
    long doctorId, patientId, id;
    int status;


    String firstName, lastName, profileImage;

    boolean isDoctor;


    public Appointment() {
    }

    public Appointment(String appointmentTitle, String startTime, String endTime, long doctorId, long patientId) {
        this.appointmentTitle = appointmentTitle;
        this.startTime = startTime;
        this.endTime = endTime;
        this.doctorId = doctorId;
        this.patientId = patientId;
    }

    public Appointment(String appointmentTitle, String startTime, String endTime, long doctorId, long patientId, long id, int status) {
        this.appointmentTitle = appointmentTitle;
        this.startTime = startTime;
        this.endTime = endTime;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.id = id;
        this.status = status;
    }

    public Appointment(String appointmentTitle, String startTime, String endTime, long doctorId, long patientId, long id, int status, String firstName, String lastName, String profileImage, boolean isDoctor) {
        this.appointmentTitle = appointmentTitle;
        this.startTime = startTime;
        this.endTime = endTime;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.id = id;
        this.status = status;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImage = profileImage;
        this.isDoctor = isDoctor;
    }

    public Appointment(String appointmentTitle, String appointmentAbout, String startTime, String endTime, long doctorId, long patientId, long id, int status, String firstName, String lastName, String profileImage, boolean isDoctor) {
        this.appointmentTitle = appointmentTitle;
        this.appointmentAbout = appointmentAbout;
        this.startTime = startTime;
        this.endTime = endTime;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.id = id;
        this.status = status;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImage = profileImage;
        this.isDoctor = isDoctor;
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

    public long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(long doctorId) {
        this.doctorId = doctorId;
    }

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public boolean isDoctor() {
        return isDoctor;
    }

    public void setDoctor(boolean doctor) {
        isDoctor = doctor;
    }

    public String getAppointmentAbout() {
        return appointmentAbout;
    }

    public void setAppointmentAbout(String appointmentAbout) {
        this.appointmentAbout = appointmentAbout;
    }
}

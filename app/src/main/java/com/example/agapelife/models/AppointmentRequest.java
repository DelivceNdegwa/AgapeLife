package com.example.agapelife.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// For appointment lists
public class AppointmentRequest {

    long id;
    long clientId;
    long doctorId;
    int status;
    String about;
    String persistencePeriod, clientFirstName, clientLastName, profile_image;
    boolean read;
    String symptoms;

    public AppointmentRequest() {
    }

    public AppointmentRequest(long id, long clientId, long doctorId, int status, String about, boolean read, String persistencePeriod, String symptoms) {
        this.id = id;
        this.clientId = clientId;
        this.doctorId = doctorId;
        this.status = status;
        this.about = about;
        this.read = read;
        this.persistencePeriod = persistencePeriod;
        this.symptoms = symptoms;
    }

    public AppointmentRequest(long id, long clientId, long doctorId, int status, String about, boolean read, String persistencePeriod, String symptoms, String clientFirstName, String clientLastName, String profileImage) {
        this.id = id;
        this.clientId = clientId;
        this.doctorId = doctorId;
        this.status = status;
        this.about = about;
        this.read = read;
        this.persistencePeriod = persistencePeriod;
        this.clientFirstName = clientFirstName;
        this.clientLastName = clientLastName;
        this.symptoms = symptoms;
        this.profile_image = profileImage;
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

    public String getPersistencePeriod() {
        return persistencePeriod;
    }

    public void setPersistencePeriod(String persistencePeriod) {
        this.persistencePeriod = persistencePeriod;
    }

    public String getClientFirstName() {
        return clientFirstName;
    }

    public void setClientFirstName(String clientFirstName) {
        this.clientFirstName = clientFirstName;
    }

    public String getClientLastName() {
        return clientLastName;
    }

    public void setClientLastName(String clientLastName) {
        this.clientLastName = clientLastName;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }
}

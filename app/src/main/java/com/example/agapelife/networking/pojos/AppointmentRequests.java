package com.example.agapelife.networking.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppointmentRequests {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("about")
    @Expose
    private String about;
    @SerializedName("read")
    @Expose
    private Boolean read;
    @SerializedName("client")
    @Expose
    private Integer client;
    @SerializedName("doctor")
    @Expose
    private Integer doctor;
    @SerializedName("symptoms")
    @Expose
    private Integer symptoms;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Integer getClient() {
        return client;
    }

    public void setClient(Integer client) {
        this.client = client;
    }

    public Integer getDoctor() {
        return doctor;
    }

    public void setDoctor(Integer doctor) {
        this.doctor = doctor;
    }

    public Integer getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(Integer symptoms) {
        this.symptoms = symptoms;
    }
}

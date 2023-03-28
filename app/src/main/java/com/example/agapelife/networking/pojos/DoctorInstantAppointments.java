package com.example.agapelife.networking.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoctorInstantAppointments {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("patient")
    @Expose
    private Patient patient;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("symptoms")
    @Expose
    private String symptoms;
    @SerializedName("suspected_illness")
    @Expose
    private String suspectedIllness;
    @SerializedName("prescription")
    @Expose
    private String prescription;
    @SerializedName("recommendation")
    @Expose
    private String recommendation;
    @SerializedName("doctor")
    @Expose
    private Integer doctor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getSuspectedIllness() {
        return suspectedIllness;
    }

    public void setSuspectedIllness(String suspectedIllness) {
        this.suspectedIllness = suspectedIllness;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public Integer getDoctor() {
        return doctor;
    }

    public void setDoctor(Integer doctor) {
        this.doctor = doctor;
    }

}
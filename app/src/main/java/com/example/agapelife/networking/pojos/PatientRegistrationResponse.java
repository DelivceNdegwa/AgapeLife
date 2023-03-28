package com.example.agapelife.networking.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PatientRegistrationResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("patient_id")
    @Expose
    private Integer patientId;

    @SerializedName("is_created")
    @Expose
    private Boolean isCreated;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Boolean getCreated() {
        return isCreated;
    }

    public void setCreated(Boolean created) {
        isCreated = created;
    }
}

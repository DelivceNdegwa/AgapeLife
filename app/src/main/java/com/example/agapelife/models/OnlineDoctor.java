package com.example.agapelife.models;

// List of all online doctors
public class OnlineDoctor {
    long id, idNumber, phoneNumber;
    boolean isVerified, isAvailable;

    public OnlineDoctor() {
    }

    public OnlineDoctor(long id, long idNumber, long phoneNumber, boolean isVerified, boolean isAvailable) {
        this.id = id;
        this.idNumber = idNumber;
        this.phoneNumber = phoneNumber;
        this.isVerified = isVerified;
        this.isAvailable = isAvailable;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(long idNumber) {
        this.idNumber = idNumber;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}

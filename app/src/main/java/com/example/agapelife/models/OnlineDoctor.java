package com.example.agapelife.models;



public class OnlineDoctor {
    private long id, idNumber, phoneNumber;
    private boolean isVerified, isAvailable;
    private String profileImage, firstName, lastName, hospital, specialization;


    public OnlineDoctor() {
    }

    public OnlineDoctor(long id, long idNumber, long phoneNumber, boolean isVerified, boolean isAvailable, String profileImage, String firstName, String lastName, String hospital, String specialization) {
        this.id = id;
        this.idNumber = idNumber;
        this.phoneNumber = phoneNumber;
        this.isVerified = isVerified;
        this.isAvailable = isAvailable;
        this.profileImage = profileImage;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hospital = hospital;
        this.specialization = specialization;
    }

    public OnlineDoctor(long id, long idNumber, long phoneNumber, boolean isVerified, boolean isAvailable, String profileImage, String firstName, String lastName) {
        this.id = id;
        this.idNumber = idNumber;
        this.phoneNumber = phoneNumber;
        this.isVerified = isVerified;
        this.isAvailable = isAvailable;
        this.profileImage = profileImage;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public OnlineDoctor(long id, long idNumber, long phoneNumber, boolean isVerified, boolean isAvailable, String firstName, String lastName) {
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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
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

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}

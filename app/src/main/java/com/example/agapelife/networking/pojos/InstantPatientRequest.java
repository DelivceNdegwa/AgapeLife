package com.example.agapelife.networking.pojos;

public class InstantPatientRequest {
    String firstName, lastName;
    long nationalID, age, phone, doctorID;


    public InstantPatientRequest() {
    }

    public InstantPatientRequest(String firstName, String lastName, long nationalID, long age, long phone, long doctorID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationalID = nationalID;
        this.age = age;
        this.phone = phone;
        this.doctorID = doctorID;
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

    public long getNationalID() {
        return nationalID;
    }

    public void setNationalID(long nationalID) {
        this.nationalID = nationalID;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public long getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(long doctorID) {
        this.doctorID = doctorID;
    }
}

package com.example.agapelife.networking.pojos;

public class DoctorRequest {
    String licenseCertificate, profileImage, hospital, speciality, category, username, phoneNumber, idNumber, password, password2, email, firstName, lastName;

    public DoctorRequest(String licenseCertificate, String profileImage, String hospital, String speciality, String category, String username, String phoneNumber, String idNumber, String password, String password2, String email, String firstName, String lastName) {
        this.licenseCertificate = licenseCertificate;
        this.profileImage = profileImage;
        this.hospital = hospital;
        this.speciality = speciality;
        this.category = category;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.idNumber = idNumber;
        this.password = password;
        this.password2 = password2;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public DoctorRequest(String licenseCertificate, String profileImage, String idNumber) {
        this.licenseCertificate = licenseCertificate;
        this.profileImage = profileImage;
        this.idNumber = idNumber;
    }

    public DoctorRequest(String hospital, String speciality, String category, String username, String phoneNumber, String idNumber, String password, String password2, String email, String firstName, String lastName) {
        this.hospital = hospital;
        this.speciality = speciality;
        this.category = category;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.idNumber = idNumber;
        this.password = password;
        this.password2 = password2;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public DoctorRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public DoctorRequest() {
    }

    public String getLicenseCertificate() {
        return licenseCertificate;
    }

    public void setLicenseCertificate(String licenseCertificate) {
        this.licenseCertificate = licenseCertificate;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        speciality = speciality;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}

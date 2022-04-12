package com.example.agapelife.models;


public class AgapeUser{
    private Integer id;
    private String username, phoneNumber, idNumber, password, password2, email, firstName, lastName;

    public AgapeUser(Integer id, String username, String phoneNumber, String idNumber, String password, String password2, String email, String firstName, String lastName) {
        this.id = id;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.idNumber = idNumber;
        this.password = password;
        this.password2 = password2;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public AgapeUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AgapeUser(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

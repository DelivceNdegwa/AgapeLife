package com.example.agapelife.validations;

import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class ValidateInput {
    TextInputEditText username, phoneNumber, idNumber, passwordtxt, passwordtxt2, emailtxt, firstName, lastName, inputSpeciality,  inputHospital, experienceYears, aboutDoctor;
    public boolean validated = true;

    public ValidateInput(TextInputEditText username, TextInputEditText passwordtxt) {
        this.username = username;
        this.passwordtxt = passwordtxt;
    }

    public ValidateInput(TextInputEditText username, TextInputEditText phoneNumber, TextInputEditText idNumber, TextInputEditText passwordtxt, TextInputEditText passwordtxt2, TextInputEditText emailtxt, TextInputEditText firstName, TextInputEditText lastName) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.idNumber = idNumber;
        this.passwordtxt = passwordtxt;
        this.passwordtxt2 = passwordtxt2;
        this.emailtxt = emailtxt;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public ValidateInput(TextInputEditText username, TextInputEditText phoneNumber, TextInputEditText idNumber, TextInputEditText passwordtxt, TextInputEditText passwordtxt2, TextInputEditText emailtxt, TextInputEditText firstName, TextInputEditText lastName, TextInputEditText inputSpeciality, TextInputEditText inputHospital, TextInputEditText experienceYears, TextInputEditText aboutDoctor) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.idNumber = idNumber;
        this.passwordtxt = passwordtxt;
        this.passwordtxt2 = passwordtxt2;
        this.emailtxt = emailtxt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.inputSpeciality = inputSpeciality;
        this.inputHospital = inputHospital;
        this.experienceYears = experienceYears;
        this.aboutDoctor = aboutDoctor;
    }

    public boolean validateLogin(){
        boolean validatedLogin = false;
        validateFirstName();
        validateLastName();

        if(validateFirstName() && validateLastName()){
            validatedLogin = true;
        }

        return validatedLogin;
    }

    public boolean validateUserSignUp(){
        boolean validatedUserSignUp = false;

        if(
                validateLastName()
                && validateLastName()
                && validateId()
                && validateEmail()
                && validatePhone()
                && validatePassword()
        ){
            validatedUserSignUp = true;
        }

        return validatedUserSignUp;
    }

    public boolean validateDoctorSignUp(){
        boolean validateDrCredentials = false;

        if(
                validateLastName()
                && validateLastName()
                && validateId()
                && validateEmail()
                && validatePhone()
                && validatePassword()
                && validateSpecialization()
                && validateHospital()
                && validateExperienceYears()
                && validateAboutDoctor()
        ){
            validateDrCredentials = true;
        }

        return validateDrCredentials;
    }


    public boolean validateFirstName(){
        if(firstName.getText().toString().trim().isEmpty()){
            firstName.setError("Please input your first name");
            validated = false;
        }
        return validated;
    }

    public boolean validateLastName(){
        if(lastName.getText().toString().trim().isEmpty()){
            lastName.setError("Please input your last name");
            validated = false;
        }
        return validated;
    }

    public boolean validatePhone(){
        if(phoneNumber.getText().toString().trim().isEmpty()){
            phoneNumber.setError("Please input your phone number");
            validated = false;
        }
        return validated;
    }

    public boolean validateEmail(){
        if(emailtxt.getText().toString().trim().isEmpty()){
            emailtxt.setError("Please input your email");
            validated = false;
        }
        else if(!emailtxt.getText().toString().trim().contains("@")){
            emailtxt.setError("Please provide a valid email");
        }
        else{
            validated = true;
        }
        return validated;
    }

    public boolean validateId(){
        if(idNumber.getText().toString().trim().isEmpty()){
            idNumber.setError("Please input your ID number");
            validated = false;
        }
        return validated;
    }

    public boolean validatePassword(){
         if(passwordtxt.getText().toString().trim().isEmpty()){
            passwordtxt.setError("Please enter a password");
            validated = false;
        }
        else if(!passwordtxt.getText().toString().trim().contentEquals(passwordtxt2.getText().toString().trim())){
            passwordtxt2.setError("Password did not match");
            validated = false;
        }
        else{
            validated = true;
         }
        return validated;
    }

    public boolean validateSpecialization(){
        if(inputSpeciality.getText().toString().isEmpty()){
            validated=false;
            inputSpeciality.setError("Please input your specialization");
        }

        return validated;
    }

    public boolean validateHospital(){
        if(inputHospital.getText().toString().isEmpty()){
            validated = false;
            inputHospital.setError("Please input your hospital");
        }

        return validated;
    }

    public boolean validateExperienceYears(){
        if(experienceYears.getText().toString().isEmpty()){
            validated = false;
            experienceYears.setError("Please input your work experience");
        }
        return validated;
    }

    public boolean validateAboutDoctor(){
        if(aboutDoctor.getText().toString().isEmpty()){
            validated = false;
            aboutDoctor.setError("Please provide about doctor details");
        }
        return validated;
    }

}

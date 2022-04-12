package com.example.agapelife;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agapelife.utils.AnimationsConfig;
import com.example.agapelife.utils.FirebaseConfig;
import com.google.android.material.textfield.TextInputEditText;

public class DoctorsFormActivity extends AppCompatActivity {

    TextInputEditText inputSpeciality, inputHospital, inputEmail, inputPassword, inputConfirmPassword, inputFirstName, inputLastName, inputIdNumber, inputPhoneNumber;
    ProgressDialog progress;
    ImageView imgAttachment, imgProfile;
    TextView profileText, licenseText;
    Button btnSignUp, btnLogin;
    ConstraintLayout signUpLayout;
    ConstraintLayout licenseAttachmentLayout, profileAttachmentLayout;
    AnimationsConfig animationsConfig = new AnimationsConfig();

    final static int DOCUMENT_CODE = 177;
    final int GALLERY_REQUEST_CODE = 100;

    FirebaseConfig firebaseConfig = new FirebaseConfig(this);

    @Override
    protected void onStart() {
        super.onStart();
//        if(firebaseConfig.userIsLoggedIn()){
//            Intent intent = new Intent(DoctorsFormActivity.this, MainActivity.class);
//            startActivity(intent);
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_form);

        progress = new ProgressDialog(this);
        progress.setMessage("Signing you up...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog


        btnLogin = findViewById(R.id.btn_login);
        btnSignUp = findViewById(R.id.btn_signup);
        signUpLayout = findViewById(R.id.signup_layout);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.input_password);
        inputConfirmPassword = findViewById(R.id.input_confirm_password);
        inputFirstName = findViewById(R.id.first_name);
        inputLastName = findViewById(R.id.l_name);
        inputIdNumber = findViewById(R.id.id_number);
        inputPhoneNumber = findViewById(R.id.phone_number);
        imgAttachment = findViewById(R.id.license_layout);
        licenseAttachmentLayout = findViewById(R.id.license_attachment);
        inputHospital = findViewById(R.id.input_hospital);
        inputSpeciality = findViewById(R.id.input_speciality);
//        imgProfile = findViewById(R.id.img_doctor);
//        profileText = findViewById(R.id.profile_text);
//        profileAttachmentLayout = findViewById(R.id.profile_section);
        licenseText = findViewById(R.id.license_text);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorsFormActivity.this, LoginSignUpActivity.class);
                startActivity(intent);
            }
        });

        profileAttachmentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyPermissions();
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInputs(inputSpeciality, inputHospital, inputEmail, inputPassword, inputConfirmPassword, inputFirstName, inputLastName, inputIdNumber, inputPhoneNumber);

            }
        });

        licenseAttachmentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDoc();
                if(pickDoc()){
                    licenseText.setVisibility(View.GONE);
                }
            }
        });
        
    }

    private boolean pickDoc() {
        boolean response = true;

        Intent pickDocument = new Intent(Intent.ACTION_GET_CONTENT);
        pickDocument.setType("application/*");
        pickDocument.addCategory(Intent.CATEGORY_OPENABLE);

        String[] mimeTypes = {
                "application/pdf",
                "application/xls",
                "application/docx",
                "application/doc",
        };

        pickDocument.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        try{
            startActivityForResult(pickDocument, DOCUMENT_CODE);

        }
        catch (ActivityNotFoundException e){
            Toast.makeText(this, "No application found to open files", Toast.LENGTH_SHORT).show();
            response = false;
        }

        return response;
    }

    private void validateInputs(TextInputEditText inputSpeciality, TextInputEditText inputHospital, TextInputEditText inputEmail, TextInputEditText inputPassword, TextInputEditText confirmPassword, TextInputEditText firstName, TextInputEditText lastName, TextInputEditText idNumber, TextInputEditText phoneNumber) {
        boolean result = true;

        if(inputEmail.getText().toString().trim().isEmpty()){
            inputEmail.setError("Please enter your email");
            result=false;
        }
        else if(inputPassword.getText().toString().trim().isEmpty()){
            inputEmail.setError("Please enter a password");
            result = false;
        }
        else if(inputPassword.getText().toString().trim().contentEquals(confirmPassword.getText().toString().trim())){
            confirmPassword.setError("Password did not match");
            result = false;
        }
        else if(firstName.getText().toString().trim().isEmpty()){
            firstName.setError("Please input first name");
        }
        else if(lastName.getText().toString().trim().isEmpty()){
            lastName.setError("Please input your last name");
        }
        else if(idNumber.getText().toString().trim().isEmpty()){
            idNumber.setError("Please input your ID Number");
        }
        else if(phoneNumber.getText().toString().trim().isEmpty()){
            phoneNumber.setError("Please input your phone number");
        }
        else if(inputHospital.getText().toString().trim().isEmpty()){
            inputHospital.setError("Please input place of work");
        }
        else if(inputSpeciality.getText().toString().trim().isEmpty()){
            inputSpeciality.setError("Please input your speciality");
        }
        else{
            firebaseConfig.createNewUser(inputEmail.getText().toString(), inputPassword.getText().toString(), "Signing you up...");
        }
    }

    public void verifyPermissions(){
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        if(
                ContextCompat.checkSelfPermission(
                        this.getApplicationContext(), permissions[0])
                        == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                        this.getApplicationContext(), permissions[1])
                        == PackageManager.PERMISSION_GRANTED
        ){
            pickImageFromGallery();
        }
        else{
            ActivityCompat.requestPermissions(this, permissions, 134);
        }
    }

    private void pickImageFromGallery() {
        Intent pickFromGallery = new Intent(Intent.ACTION_PICK);
        pickFromGallery.setType("image/*");
        String[] mimeTypes = {
                "image/jpeg",
                "image/png",
                "image/pdf",        };

        try{
            startActivityForResult(pickFromGallery, GALLERY_REQUEST_CODE);
        }catch(ActivityNotFoundException e){
            //
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK){
            profileText.setVisibility(View.GONE);
            Uri uri = data.getData();
            imgProfile.setImageURI(uri);

        }
    }


}
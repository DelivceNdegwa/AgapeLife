package com.example.agapelife;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
//import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.agapelife.networking.pojos.DoctorRequest;
import com.example.agapelife.networking.services.ServiceGenerator;
import com.example.agapelife.utils.AnimationsConfig;
import com.example.agapelife.utils.PreferenceStorage;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorsFormActivity extends AppCompatActivity {

    TextInputEditText inputSpeciality, inputHospital, inputEmail, inputPassword, inputConfirmPassword, inputFirstName, inputLastName, inputIdNumber, inputPhoneNumber;
    ProgressDialog progress;
    ImageView imgLicenseAttachment, imgProfile;
    TextView profileText, licenseText;
    Button btnSignUp, btnLogin;
    ConstraintLayout signUpLayout;
    ConstraintLayout licenseAttachmentLayout, profileAttachmentLayout;
    AnimationsConfig animationsConfig = new AnimationsConfig();

    PreferenceStorage preferenceStorage;

    String specialization, hospitalTxt, email, password, confirmPasswordTxt, firstNameTxt, lastNameTxt, idNumberTxt, phoneNumberTxt;
    MultipartBody.Part licenseFile, profileImage;

    final static int DOCUMENT_CODE = 177;
    int GALLERY_REQUEST_CODE = 100;
    String encodedPdf, profileImageUri;
    Uri licenseUri, selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_form);

        progress = new ProgressDialog(this);
        progress.setMessage("Signing you up...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog

        preferenceStorage = new PreferenceStorage(this);

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
        imgLicenseAttachment = findViewById(R.id.license_layout);
        licenseAttachmentLayout = findViewById(R.id.license_attachment);
        inputHospital = findViewById(R.id.input_hospital);
        inputSpeciality = findViewById(R.id.input_speciality);
        imgProfile = findViewById(R.id.img_attatchment);
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

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
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
            }
        });
        
    }

    private void validateInputs(TextInputEditText inputSpeciality, TextInputEditText inputHospital, TextInputEditText inputEmail, TextInputEditText inputPassword, TextInputEditText confirmPassword, TextInputEditText firstName, TextInputEditText lastName, TextInputEditText idNumber, TextInputEditText phoneNumber) {
        boolean result = true;

        email = inputEmail.getText().toString();
        password = inputPassword.getText().toString();
        confirmPasswordTxt = confirmPassword.getText().toString();
        firstNameTxt = firstName.getText().toString();
        lastNameTxt = lastName.getText().toString();
        idNumberTxt = idNumber.getText().toString();
        phoneNumberTxt = phoneNumber.getText().toString();
        hospitalTxt = inputHospital.getText().toString();
        specialization = inputSpeciality.getText().toString();


        if(email.trim().isEmpty()){
            inputEmail.setError("Please enter your email");
            result=false;
        }
        else if(password.trim().isEmpty()){
            inputEmail.setError("Please enter a password");
            result = false;
        }
        else if(!password.trim().contentEquals(confirmPasswordTxt.trim())){
            confirmPassword.setError("Password did not match");
            result = false;
        }
        else if(firstNameTxt.trim().isEmpty()){
            firstName.setError("Please input first name");
        }
//        else if(licenseUri.equals(Uri.EMPTY)){
//            Toast.makeText(this, "URI"+String.valueOf(licenseUri), Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, "Please input your medical license", Toast.LENGTH_SHORT).show();
//        }
        else if(lastNameTxt.trim().isEmpty()){
            lastName.setError("Please input your last name");
        }
        else if(idNumberTxt.trim().isEmpty()){
            idNumber.setError("Please input your ID Number");
        }
        else if(phoneNumberTxt.trim().isEmpty()){
            phoneNumber.setError("Please input your phone number");
        }
        else if(hospitalTxt.trim().isEmpty()){
            inputHospital.setError("Please input place of work");
        }
        else if(specialization.trim().isEmpty()){
            inputSpeciality.setError("Please input your specialization");
        }
        else{
            takeFormDetails();
        }
    }


    public boolean verifyPermissions(){
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE
        };

        boolean isPermitted = false;

        if(
                ContextCompat.checkSelfPermission(
                        this.getApplicationContext(), permissions[0])
                        == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                        this.getApplicationContext(), permissions[1])
                        == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                        this.getApplicationContext(), permissions[2])
                        == PackageManager.PERMISSION_GRANTED
        ){
            isPermitted = true;
        }
        else{
            ActivityCompat.requestPermissions(this, permissions, 134);
        }

        return isPermitted;
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

    public boolean pickDoc(){
        verifyPermissions();
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

    public void imageChooser() {
        verifyPermissions();
        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select an image"), GALLERY_REQUEST_CODE);
    }

    private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK  && data != null){
            //profileText.setVisibility(View.GONE);
            Uri uri = data.getData();

            selectedImageUri = uri;
//            imgProfile.setImageURI(uri);
            Glide.with(this).load(uri)
                    .error(android.R.drawable.ic_dialog_alert)
                    .centerCrop()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(imgProfile);
            profileImageUri = String.valueOf(selectedImageUri);


        }

        else if(requestCode == DOCUMENT_CODE && resultCode == RESULT_OK && data != null){
            licenseUri = data.getData();
            encodedPdf = String.valueOf(licenseUri);

            imgLicenseAttachment.setBackgroundResource(R.drawable.ic_success_doc);

            licenseText.setVisibility(View.GONE);
        }
        else{
            // None
        }
    }

    @NonNull
    private RequestBody createPartFromString(String fieldString){
        return RequestBody.create(MultipartBody.FORM, fieldString);
    }

    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri){
        File fetchedFile = new File(getPathFromURI(fileUri));
        RequestBody filePart = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), fetchedFile);

        return MultipartBody.Part.createFormData(partName, fetchedFile.getName(), filePart);
    }

    public void takeFormDetails(){
        progress.show();
        licenseFile = prepareFilePart("license_certificate", licenseUri);
        profileImage = prepareFilePart("profile_image", selectedImageUri);

        //is_verified, license_certificate, profile_image, , experience_years, , , , ,self_description, is_available

        RequestBody hospitalResponse = createPartFromString(hospitalTxt);
        RequestBody specializationResponse = createPartFromString(specialization);
        RequestBody firstNameRequest = createPartFromString(firstNameTxt);
        RequestBody lastNameRequest = createPartFromString(lastNameTxt);
        RequestBody categoryRequest = createPartFromString("3");
        RequestBody phoneRequest = createPartFromString(phoneNumberTxt);
        RequestBody idNumberRequest = createPartFromString(idNumberTxt);
        RequestBody passwordRequest = createPartFromString(password);
        RequestBody confirmPasswordRequest = createPartFromString(confirmPasswordTxt);
        RequestBody emailRequest = createPartFromString(email);


        Map<String, RequestBody> doctorDetails = new HashMap<>();

        doctorDetails.put("hospital", hospitalResponse);
        doctorDetails.put("speciality", specializationResponse);
        doctorDetails.put("category", categoryRequest);
        doctorDetails.put("username", firstNameRequest);
        doctorDetails.put("email", emailRequest);
        doctorDetails.put("first_name", firstNameRequest);
        doctorDetails.put("last_name", lastNameRequest);
        doctorDetails.put("password", passwordRequest);
        doctorDetails.put("password2", confirmPasswordRequest);
        doctorDetails.put("id_number", idNumberRequest);
        doctorDetails.put("phone_number", phoneRequest);
        //registerDoctor(doctorDetails, licenseFile, profileImg);

        RequestBody description = createPartFromString("hello, this is description speaking");
        RequestBody place = createPartFromString("Magdeburg");
        RequestBody time = createPartFromString("2016");

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("description", description);
        map.put("place", place);
        map.put("time", time);
        uploadDoctorDetails();

    }

    public void uploadDoctorDetails(){
        Call<DoctorRequest> call = ServiceGenerator.getInstance().getApiConnector().doctorForm(
                hospitalTxt,
                "3",
                specialization,
                firstNameTxt,
                phoneNumberTxt,
                idNumberTxt,
                password,
                confirmPasswordTxt,
                email,
                firstNameTxt,
                lastNameTxt
        );
        call.enqueue(new Callback<DoctorRequest>() {
            @Override
            public void onResponse(Call<DoctorRequest> call, Response<DoctorRequest> response) {
                Log.d("DOC_FORM_CODE:", String.valueOf(response.code()));
//                Toast.makeText(DoctorsFormActivity.this, String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                if(response.code() == 201){
                    uploadDoctorFiles();
                }
                else if(response.code() == 400 && response.body() != null){
                    progress.hide();
                    Log.d("DOC_FORM_ERROR: ", String.valueOf(response.body()));
                    Toast.makeText(DoctorsFormActivity.this, "Check your credentials and try again", Toast.LENGTH_SHORT).show();
                }
                else{
                    progress.hide();
                    Toast.makeText(DoctorsFormActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DoctorRequest> call, Throwable t) {
                Log.d("DOC_FORM_ERROR:", String.valueOf(t.getStackTrace()));
            }
        });
    }

    private void uploadDoctorFiles() {
        Call<DoctorRequest> call = ServiceGenerator.getInstance().getApiConnector().uploadDocFiles(
                Long.parseLong(idNumberTxt),
                licenseFile,
                profileImage
        );
        call.enqueue(new Callback<DoctorRequest>() {
            @Override
            public void onResponse(Call<DoctorRequest> call, Response<DoctorRequest> response) {
                Log.d("DOC_FILES_CODE:", String.valueOf(response.code()));
//                Toast.makeText(DoctorsFormActivity.this, String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                if(response.code() == 201){
                    preferenceStorage.saveUserId(idNumberTxt);
                    //preferenceStorage.
                    preferenceStorage.setLoggedInStatus(true);
                    preferenceStorage.setIsDoctor(true);
                    preferenceStorage.saveUserDetails(firstNameTxt, lastNameTxt);
                    preferenceStorage.setUserId(idNumberTxt);
                    preferenceStorage.saveLoginData(firstNameTxt, confirmPasswordTxt);
                    progress.hide();
                    Intent intent = new Intent(DoctorsFormActivity.this, DoctorsSection.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(DoctorsFormActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DoctorRequest> call, Throwable t) {
                progress.hide();
                Log.d("DOC_FILES_ERROR:", String.valueOf(t.getStackTrace()));
            }
        });
    }

    public void registerDoctor(Map<String, RequestBody> doctorDetails, MultipartBody.Part licenseFile, MultipartBody.Part profileImg){
        Log.d("DATA_MAP", String.valueOf(doctorDetails));
        Call<ResponseBody> call = ServiceGenerator.getInstance().getApiConnector().registerDoctor(doctorDetails,licenseFile, profileImg);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progress.hide();
                if(response.code() == 201){
                    Toast.makeText(DoctorsFormActivity.this, "Your details have been submitted, we will get back to you", Toast.LENGTH_SHORT).show();

                    preferenceStorage.saveUserId(idNumberTxt);
                    //preferenceStorage.
                    preferenceStorage.setLoggedInStatus(true);
                    preferenceStorage.setIsDoctor(true);
                    preferenceStorage.saveUserDetails(firstNameTxt, lastNameTxt);
                    preferenceStorage.setUserId(idNumberTxt);
                    preferenceStorage.saveLoginData(firstNameTxt, confirmPasswordTxt);

                    Intent intent = new Intent(DoctorsFormActivity.this, DoctorsSection.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(DoctorsFormActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Log.d("onResponseErrorStatus", String.valueOf(response.code()));
                    Log.d("onResponseErrorBody", String.valueOf(response));

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progress.hide();
                Toast.makeText(DoctorsFormActivity.this, "Check your connection and try again", Toast.LENGTH_SHORT).show();
                Log.d("onFailureThrowable", t.toString());
                Log.d("onFailureStackTrace", String.valueOf(t.getStackTrace()));
            }
        });
    }

}
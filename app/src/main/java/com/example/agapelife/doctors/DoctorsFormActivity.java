package com.example.agapelife.doctors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;


import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.agapelife.R;
import com.example.agapelife.authentication.LoginSignUpActivity;
import com.example.agapelife.networking.pojos.DoctorRequest;
import com.example.agapelife.networking.services.ServiceGenerator;
import com.example.agapelife.utils.AnimationsConfig;
import com.example.agapelife.utils.PreferenceStorage;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    ImageView imgLicenseAttachment, imgProfile, imgDOBPicker;
    TextView profileText, licenseText, inputAge;
    Button btnSignUp, btnLogin;
    ConstraintLayout signUpLayout;
    ConstraintLayout licenseAttachmentLayout, profileAttachmentLayout;
    AnimationsConfig animationsConfig = new AnimationsConfig();
    RadioGroup genderGroup;

    PreferenceStorage preferenceStorage;

    String specialization, hospitalTxt, email, password, ageTxt, confirmPasswordTxt, firstNameTxt, lastNameTxt, idNumberTxt, phoneNumberTxt;
    MultipartBody.Part licenseFile, profileImage;

    int genderId, genderChoice, ageValue;

    final static int DOCUMENT_CODE = 177;
    int GALLERY_REQUEST_CODE = 100;
    String encodedPdf, profileImageUri, dateOfBirth;
    Uri licenseUri, selectedImageUri;

    private DatePickerDialog datePickerDialog;

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
        imgDOBPicker = findViewById(R.id.iv_dob_picker);
//        profileText = findViewById(R.id.profile_text);
//        profileAttachmentLayout = findViewById(R.id.profile_section);
        licenseText = findViewById(R.id.license_text);

        inputAge = findViewById(R.id.input_age);
        genderGroup = findViewById(R.id.radio_gender_group);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorsFormActivity.this, LoginSignUpActivity.class);
                startActivity(intent);
            }
        });

        if (genderGroup != null) {
            genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    genderChoice = (R.id.rb_male == checkedId) ? 1 : 2;
                    genderId = checkedId;
                }
            });
        }


        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });



        imgDOBPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDatePicker();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInputs(genderGroup, inputSpeciality, inputHospital, inputEmail, inputPassword, inputConfirmPassword, inputFirstName, inputLastName, inputIdNumber, inputPhoneNumber, inputAge);
            }
        });

        licenseAttachmentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDoc();
            }
        });
        
    }


    private void validateInputs(RadioGroup genderGroup, TextInputEditText inputSpeciality, TextInputEditText inputHospital, TextInputEditText inputEmail, TextInputEditText inputPassword, TextInputEditText confirmPassword, TextInputEditText firstName, TextInputEditText lastName, TextInputEditText idNumber, TextInputEditText phoneNumber, TextView inputAge) {
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
        ageTxt = inputAge.getText().toString();


        if(email.trim().isEmpty()){
            inputEmail.setError("Please enter your email");
            result=false;
        }
        else if(genderId != R.id.rb_male && genderId != R.id.rb_female){
            Toast.makeText(this, "Please input your gender", Toast.LENGTH_LONG).show();
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
        else if(ageTxt.trim().isEmpty()){
            Toast.makeText(this, "Please input date of birth", Toast.LENGTH_SHORT).show();
        }
        else if(licenseUri == null || Uri.EMPTY.equals(licenseUri)){
            Toast.makeText(this, "Please input license", Toast.LENGTH_SHORT).show();
        }
        else if(profileImageUri == null || Uri.EMPTY.equals(profileImageUri)){
            Toast.makeText(this, "Please input profile image", Toast.LENGTH_SHORT).show();
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
        uploadDoctorDetails();

    }

    public void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month += 1;
                calculateDateOfBirth(year, month, day);
            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        new DatePickerDialog(this, style, dateSetListener, year, month, day).show();
    }

    private void calculateDateOfBirth(int year, int month, int day) {
        makeDateString(year, month, day);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate today = LocalDate.now();
            LocalDate birthday = LocalDate.of(year, month, day);

            Period period = Period.between(birthday, today);
            ageValue = period.getYears();
        }

    }

    private void makeDateString(int year, int month, int day) {
        String date = getMonthFormat(month)+" "+day+", "+year;
        dateOfBirth = year+"-"+month+"-"+day;
        inputAge.setText(date);
    }

    private String getMonthFormat(int month) {
        String [] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        int[] monthNumbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

        Map<Integer, String> months = IntStream.range(0, monthNumbers.length).boxed()
                .collect(Collectors.toMap(i -> monthNumbers[i], i -> monthNames[i]));

        return months.get(month);
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
                lastNameTxt,
                genderChoice,
                ageValue,
                dateOfBirth
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
                    savePreferenceData();
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

    private void savePreferenceData() {
        try {
            preferenceStorage.saveUserId(idNumberTxt);
            //preferenceStorage.
            preferenceStorage.setLoggedInStatus(true);
            preferenceStorage.setIsDoctor(true);
            preferenceStorage.saveUserDetails(firstNameTxt, lastNameTxt);
            preferenceStorage.setUserId(idNumberTxt);
            preferenceStorage.setDoctorGender(genderChoice);
            preferenceStorage.saveLoginData(firstNameTxt, confirmPasswordTxt);
        }
        catch (Exception e){
            Log.e("PREFERENCE_ERROR", e.toString());
            Toast.makeText(this, "Unfortunately something went wrong", Toast.LENGTH_SHORT).show();
        }
    }


    public void createRequestBodyandMap(){
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
        RequestBody ageRequest = createPartFromString(ageTxt);
        RequestBody genderRequest = createPartFromString(String.valueOf(genderId));


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
        doctorDetails.put("gender", genderRequest);
        doctorDetails.put("age", ageRequest);
        //registerDoctor(doctorDetails, licenseFile, profileImg);

        RequestBody description = createPartFromString("hello, this is description speaking");
        RequestBody place = createPartFromString("Magdeburg");
        RequestBody time = createPartFromString("2016");

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("description", description);
        map.put("place", place);
        map.put("time", time);
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
package com.example.agapelife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.agapelife.models.Doctor;
import com.example.agapelife.networking.pojos.DoctorResponse;
import com.example.agapelife.networking.services.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorDetails extends AppCompatActivity {
    int id;
    ImageView doctorProfile;
    TextView hospital, specialization, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        doctorProfile = findViewById(R.id.doc_profile);
        hospital = findViewById(R.id.details_hospital);
        specialization = findViewById(R.id.details_speciality);

        Intent intent = getIntent();
        if(intent.hasExtra("DOCTOR_ID")){
            id = intent.getIntExtra("DOCTOR_ID", 0);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        getDoctorDetails();
    }


    private void getDoctorDetails() {
        Call<DoctorResponse> call = ServiceGenerator.getInstance().getApiConnector().getDoctorDetails(id);

        call.enqueue(new Callback<DoctorResponse>() {
            @Override
            public void onResponse(Call<DoctorResponse> call, Response<DoctorResponse> response) {
                DoctorResponse doctorResponse = response.body();

                assignElementValues(doctorResponse.getProfileImage(), doctorResponse.getHospital(), doctorResponse.getSpeciality());

            }

            @Override
            public void onFailure(Call<DoctorResponse> call, Throwable t) {

            }
        });
}

    private void assignElementValues(String profileImage, String hospital_str, String speciality) {
        Glide.with(DoctorDetails.this).load(profileImage).placeholder(R.drawable.docor_img).into(doctorProfile);
        hospital.setText(hospital_str);
        specialization.setText(speciality);
    }
    }

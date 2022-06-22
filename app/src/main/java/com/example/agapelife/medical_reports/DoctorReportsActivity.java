package com.example.agapelife.medical_reports;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.agapelife.adapters.MedicalReportAdapter;
import com.example.agapelife.networking.pojos.AgapeUserResponse;
import com.example.agapelife.networking.pojos.MedicalReportResponse;
import com.example.agapelife.networking.services.ServiceGenerator;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agapelife.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class    DoctorReportsActivity extends AppCompatActivity {
    ConstraintLayout noReportsLayout;
    Toolbar toolbar;
    CollapsingToolbarLayout toolbarLayout;
    ImageView ivPatientProfile;
    RecyclerView rvReports;
    MedicalReportAdapter medicalReportAdapter;
    TextView tvNoReports, tvFullName, tvAge, tvGender;

    int patientId, doctorId;
    String fullName;

    List<MedicalReportResponse> medicalReportList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_reports);

        toolbar = findViewById(R.id.toolbar);
        toolbarLayout = findViewById(R.id.toolbar_layout);
        ivPatientProfile = findViewById(R.id.iv_patient);
        noReportsLayout = findViewById(R.id.cl_no_reports_layout);
        tvNoReports = findViewById(R.id.tv_no_reports);
        tvFullName = findViewById(R.id.tv_full_name);
        tvAge = findViewById(R.id.tv_age);
        tvGender = findViewById(R.id.tv_gender);

        setSupportActionBar(toolbar);
        toolbarLayout.setTitle(" ");

        rvReports = findViewById(R.id.rv_reports);
        rvReports.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvReports.setNestedScrollingEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        patientId = intent.getIntExtra("CLIENT_ID", 0);
        doctorId = intent.getIntExtra("DOCTOR_ID", 0);

        Log.d("REQD_CLIENT_ID", String.valueOf(patientId));
        Log.d("REQD_DOCTOR_ID", String.valueOf(doctorId));

        getPatientDetails(patientId);
        getMedicalReports(patientId);

        medicalReportAdapter = new MedicalReportAdapter(medicalReportList, this);
        rvReports.setAdapter(medicalReportAdapter);
    }

    private void getMedicalReports(int patientId) {
        Call<List<MedicalReportResponse>> call = ServiceGenerator.getInstance().getApiConnector().getDoctorsNotes(patientId);
        call.enqueue(new Callback<List<MedicalReportResponse>>() {
            @Override
            public void onResponse(Call<List<MedicalReportResponse>> call, Response<List<MedicalReportResponse>> response) {
                if(!response.body().isEmpty() && response.code()==200){
                    medicalReportList.clear();
                    medicalReportList.addAll(response.body());
                    rvReports.setVisibility(View.VISIBLE);
                    noReportsLayout.setVisibility(View.GONE);
                    medicalReportAdapter.notifyDataSetChanged();

                    for(MedicalReportResponse reportResponse: medicalReportList){
                        Log.d("REQD_DOCTOR", reportResponse.getDoctorName());
                        if(noReportsLayout.getVisibility() == View.VISIBLE){
                            Log.d("REQD_RV", "Visible");
                        }
                    }

                }
                else if(response.body() .isEmpty() && response.code() == 200){
                    rvReports.setVisibility(View.GONE);
                    noReportsLayout.setVisibility(View.VISIBLE);
                    tvNoReports.setText(fullName +" has no medical records yet");

                }
                else{
                    Toast.makeText(DoctorReportsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MedicalReportResponse>> call, Throwable t) {
                Toast.makeText(DoctorReportsActivity.this, "Check your connection and try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPatientDetails(int patientId) {
        Call<AgapeUserResponse> call = ServiceGenerator.getInstance().getApiConnector().getPatientDetails(patientId);
        call.enqueue(new Callback<AgapeUserResponse>() {
            @Override
            public void onResponse(Call<AgapeUserResponse> call, Response<AgapeUserResponse> response) {
                if(response.body() != null && response.code() == 200){
                    AgapeUserResponse userResponse = response.body();
                    fullName = userResponse.getFirstName()+" "+userResponse.getLastName();
                    toolbar.setTitle(fullName);

                    Glide.with(DoctorReportsActivity.this)
                            .load(userResponse.getProfilePhoto())
                            .placeholder(R.drawable.ic_baseline_person_24)
                            .into(ivPatientProfile);

                    tvFullName.setText(userResponse.getFirstName()+" "+userResponse.getLastName());

                }
                else{
                    Toast.makeText(DoctorReportsActivity.this, "Something went wrong, please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AgapeUserResponse> call, Throwable t) {
                Toast.makeText(DoctorReportsActivity.this, "Check your connection and try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
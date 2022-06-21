package com.example.agapelife.medical_reports;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.agapelife.adapters.MedicalReportAdapter;
import com.example.agapelife.networking.pojos.AgapeUserResponse;
import com.example.agapelife.networking.pojos.MedicalReport;
import com.example.agapelife.networking.pojos.MedicalReportResponse;
import com.example.agapelife.networking.services.ServiceGenerator;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class DoctorReportsActivity extends AppCompatActivity {
    ConstraintLayout noReportsLayout;
    Toolbar toolbar;
    CollapsingToolbarLayout toolbarLayout;
    ImageView ivPatientProfile;
    RecyclerView rvReports;
    MedicalReportAdapter medicalReportAdapter;
    TextView tvNoReports;

    int patientId, doctorId;
    String firstName;

    List<MedicalReportResponse> medicalReportList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_reports);
        // Toolbar toolbar, setSupportActionBar(toolbar), CollapsingToolbarLayout toolBarLayout, toolBarLayout.setTitle(getTitle());

        toolbar = findViewById(R.id.toolbar);
        toolbarLayout = findViewById(R.id.toolbar_layout);
        ivPatientProfile = findViewById(R.id.iv_patient);
        noReportsLayout = findViewById(R.id.cl_no_reports_layout);
        tvNoReports = findViewById(R.id.tv_no_reports);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

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

        getPatientDetails();
        getMedicalReports();

        medicalReportAdapter = new MedicalReportAdapter(medicalReportList, this);
        rvReports.setAdapter(medicalReportAdapter);
    }

    private void getMedicalReports() {
        Call<List<MedicalReportResponse>> call = ServiceGenerator.getInstance().getApiConnector().getDoctorsNotes(patientId);
        call.enqueue(new Callback<List<MedicalReportResponse>>() {
            @Override
            public void onResponse(Call<List<MedicalReportResponse>> call, Response<List<MedicalReportResponse>> response) {
                if(response.body() != null && response.code()==200){
                    medicalReportList.addAll(response.body());
                    rvReports.setVisibility(View.VISIBLE);
                    noReportsLayout.setVisibility(View.GONE);
                }
                else if(response.body() == null && response.code() == 200){
                    rvReports.setVisibility(View.GONE);
                    noReportsLayout.setVisibility(View.VISIBLE);
                    tvNoReports.setText(firstName+" has no medical records yet");

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

    private void getPatientDetails() {
        Call<AgapeUserResponse> call = ServiceGenerator.getInstance().getApiConnector().getPatientDetails(Long.valueOf(patientId));
        call.enqueue(new Callback<AgapeUserResponse>() {
            @Override
            public void onResponse(Call<AgapeUserResponse> call, Response<AgapeUserResponse> response) {
                if(response.body() != null && response.code() == 200){
                    AgapeUserResponse userResponse = response.body();
                    firstName = userResponse.getFirstName();
                    toolbar.setTitle(firstName+" details");

                    Glide.with(DoctorReportsActivity.this)
                            .load(userResponse.getProfilePhoto())
                            .placeholder(R.drawable.ic_baseline_person_24)
                            .into(ivPatientProfile);
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
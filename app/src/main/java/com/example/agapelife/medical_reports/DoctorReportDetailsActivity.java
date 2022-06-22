package com.example.agapelife.medical_reports;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.agapelife.R;
import com.example.agapelife.doctors.DoctorDetailsActivity;
import com.example.agapelife.networking.pojos.DoctorResponse;
import com.example.agapelife.networking.services.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorReportDetailsActivity extends AppCompatActivity {
    long doctorId, patientId;
    String doctorName, doctorProfileImage, doctorHospital, doctorMedication, createdAt, doctorReport, appointmentTitle;
    ImageView ivDoctorProfileImage, ivViewDoctor;
    TextView tvDoctorDetailsName, tvDoctorDetailsHospital, tvAptmtTitleTxt, tvDateTimeText, tvDoctorNotesTxt, tvMedicationGivenTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_report_details);

        ivDoctorProfileImage = findViewById(R.id.iv_doctor_profile_image);
        ivViewDoctor = findViewById(R.id.iv_view_doctor);
        tvDoctorDetailsHospital = findViewById(R.id.tv_doctor_details_hospital);
        tvDoctorDetailsName = findViewById(R.id.tv_doctor_details_name);
        tvAptmtTitleTxt = findViewById(R.id.tv_aptmt_title_txt);
        tvDateTimeText = findViewById(R.id.tv_datetime_txt);
        tvDoctorNotesTxt = findViewById(R.id.tv_doctor_notes_txt);
        tvMedicationGivenTxt = findViewById(R.id.tv_medication_given_txt);

    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent  = getIntent();
        doctorId = intent.getLongExtra("DOCTOR_ID", 0);
        doctorMedication = intent.getStringExtra("DOCTOR_MEDICATION");
        createdAt = intent.getStringExtra("CREATED_AT");
        doctorReport = intent.getStringExtra("DOCTOR_REPORT");
        appointmentTitle = intent.getStringExtra("APPOINTMENT_TITLE");
        doctorName = intent.getStringExtra("DOCTOR_NAME");

        if(doctorId != 0){
            getDoctorDetails();

            tvAptmtTitleTxt.setText(appointmentTitle);
            tvDateTimeText.setText(createdAt);
            tvMedicationGivenTxt.setText(doctorMedication);
            tvDoctorNotesTxt.setText(doctorReport);

        }
        else{
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            goBackToReports();
        }

        ivViewDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorReportDetailsActivity.this, DoctorDetailsActivity.class);
                intent.putExtra("DOCTOR_PK", doctorId);
                startActivity(intent);
            }
        });
    }

    private void goBackToReports() {
        Intent i = new Intent(DoctorReportDetailsActivity.this, DoctorReportsActivity.class);
        startActivity(i);
        finish();
    }

    private void getDoctorDetails() {
        Call<DoctorResponse> call = ServiceGenerator.getInstance().getApiConnector().getDoctorDetailsWithId(doctorId);
        call.enqueue(new Callback<DoctorResponse>() {
            @Override
            public void onResponse(Call<DoctorResponse> call, Response<DoctorResponse> response) {
                if(response.body() != null && response.code() == 200){
                    Glide.with(DoctorReportDetailsActivity.this)
                            .load(response.body().getProfileImage())
                            .placeholder(R.drawable.ic_baseline_person_24)
                            .into(ivDoctorProfileImage);

                    tvDoctorDetailsName.setText("Dr "+response.body().getFirstName());
                    tvDoctorDetailsHospital.setText(response.body().getHospital());
                }
                else{
                    Toast.makeText(DoctorReportDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    goBackToReports();
                }
            }

            @Override
            public void onFailure(Call<DoctorResponse> call, Throwable t) {
                Toast.makeText(DoctorReportDetailsActivity.this, "Check your connection and try again", Toast.LENGTH_SHORT).show();
                goBackToReports();
            }
        });
    }
}
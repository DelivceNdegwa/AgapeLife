package com.example.agapelife.appointments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.agapelife.R;
import com.example.agapelife.medical_reports.DoctorReportsActivity;

public class AppointmentRequestDetailsActivity extends AppCompatActivity {
    TextView tvAbout, tvSymptoms, tvPatientName;

    Button btnScheduleAppointment;
    String about, symptoms, patientName;
    int client_id, doctor_id, appointment_id;

    CardView cardPatientDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_request_details);

        tvAbout = findViewById(R.id.tv_request_about_txt);
        tvSymptoms = findViewById(R.id.tv_patient_request_symptoms);
        tvPatientName = findViewById(R.id.tv_doctor_details_name);

        btnScheduleAppointment = findViewById(R.id.btn_accept_request);
        cardPatientDetails = findViewById(R.id.card_patient_details);




    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();

        about = intent.getStringExtra("ABOUT");
        symptoms = intent.getStringExtra("SYMPTOMS");
        patientName = intent.getStringExtra("PATIENT_NAME");
        client_id = intent.getIntExtra("CLIENT_ID", 0);
        doctor_id = intent.getIntExtra("DOCTOR_ID", 0);
        appointment_id = intent.getIntExtra("APPOINTMENT_ID", 0);


        tvPatientName.setText(patientName);
        tvAbout.setText(about);
        tvSymptoms.setText(symptoms);

        btnScheduleAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppointmentRequestDetailsActivity.this, CreateAppointmentActivity.class);
                intent.putExtra("CLIENT_ID", client_id);
                intent.putExtra("DOCTOR_ID", doctor_id);
                intent.putExtra("PATIENT_NAME", patientName);
                intent.putExtra("APPOINTMENT_ID", appointment_id);
                startActivity(intent);
            }
        });

        cardPatientDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppointmentRequestDetailsActivity.this, DoctorReportsActivity.class);
                intent.putExtra("CLIENT_ID", client_id);
                intent.putExtra("DOCTOR_ID", doctor_id);
                startActivity(intent);
            }
        });
    }
}
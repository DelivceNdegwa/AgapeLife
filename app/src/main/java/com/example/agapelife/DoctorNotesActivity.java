package com.example.agapelife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.agapelife.networking.pojos.MedicalReport;
import com.example.agapelife.networking.services.ServiceGenerator;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorNotesActivity extends AppCompatActivity {
    TextInputEditText etvDoctorSummary, etvPrescriptions;
    Button btnCreateReport;
    long appointmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_notes);

        etvDoctorSummary = findViewById(R.id.etv_doc_summary);
        etvPrescriptions = findViewById(R.id.etv_prescriptions);
        btnCreateReport = findViewById(R.id.btn_create_report);

    }

    @Override
    protected void onResume() {
        super.onResume();
        appointmentId = getIntent().getLongExtra("APPOINTMENT_ID", 0);
        btnCreateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(appointmentId != 0){
                    validateDoctorSummary();
                }
                else{
                    Toast.makeText(DoctorNotesActivity.this, "Invalid appointment, please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void validateDoctorSummary() {
        if(etvDoctorSummary.getText().toString().trim().isEmpty()){
            etvDoctorSummary.setError("You did not write a report");
        }
        else{
            createMedicalAppointment();
        }
    }

    private void createMedicalAppointment() {
        Call<MedicalReport> call = ServiceGenerator.getInstance().getApiConnector().createMedicalReport(
                appointmentId,
                etvPrescriptions.getText().toString(),
                etvDoctorSummary.getText().toString()
        );

        call.enqueue(new Callback<MedicalReport>() {
            @Override
            public void onResponse(Call<MedicalReport> call, Response<MedicalReport> response) {
                if(response.code() == 201){
                    Toast.makeText(DoctorNotesActivity.this, "Medical Report created", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(DoctorNotesActivity.this, DoctorsSection.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(DoctorNotesActivity.this, "Could not create appointment, please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MedicalReport> call, Throwable t) {
                Toast.makeText(DoctorNotesActivity.this, "Check your connection and try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
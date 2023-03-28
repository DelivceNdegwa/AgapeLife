package com.example.agapelife.instant_appointments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agapelife.R;
import com.example.agapelife.doctors.DoctorsSection;
import com.example.agapelife.networking.pojos.CreateSuccessfullyResponse;
import com.example.agapelife.networking.services.ServiceGenerator;
import com.example.agapelife.utils.PreferenceStorage;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OngoingAppointmentActivity extends AppCompatActivity {
    String fullName, phoneNumber;
    int patientID;
    String doctorId, symptoms, suspectedIllness, prescription, recommendation;

    TextView tvFullName, tvPhoneNumber;
    TextInputEditText etSymptoms, etSuspectedIllness, etPrescription, etRecommendation;
    Button btnFinishAppointment;
    PreferenceStorage preferenceStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_appointment);

        preferenceStorage = new PreferenceStorage(OngoingAppointmentActivity.this);
        tvFullName = findViewById(R.id.tv_aptmt_patient_name);
        tvPhoneNumber = findViewById(R.id.tv_aptmt_patient_phone);

        etSymptoms = findViewById(R.id.et_aptmt_symptoms);
        etSuspectedIllness = findViewById(R.id.et_aptmt_suspected_ilness);
        etPrescription = findViewById(R.id.et_aptmt_prescription);
        etRecommendation = findViewById(R.id.et_aptmt_recommendation);
        btnFinishAppointment = findViewById(R.id.btn_finish_appointment);

        Intent intent = getIntent();
        fullName = intent.getStringExtra("FULL_NAME");
        phoneNumber = intent.getStringExtra("PHONE_NUMBER");
        patientID = intent.getIntExtra("PATIENT_DB_ID", 0);

        tvFullName.setText(fullName);
        tvPhoneNumber.setText(phoneNumber);

        btnFinishAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suspectedIllness=etSuspectedIllness.getText().toString();
                symptoms = etSymptoms.getText().toString();
                recommendation = etRecommendation.getText().toString();
                prescription = etPrescription.getText().toString();
                verifyFields();
            }
        });
    }

    private void verifyFields() {
        if(symptoms.isEmpty()){
            etSymptoms.setError("Input symptoms");
        }
        else if(suspectedIllness.isEmpty()){
            etSuspectedIllness.setError("Insert suspected illness");
        }
        else if(recommendation.isEmpty()){
            etRecommendation.setError("Insert recommendation");
        }
        else{
            submitDetails();
        }
    }

    private void submitDetails() {
        // We need: doc_id, patient_id, symptoms, suspectedIllness, prescription and recommendation
        doctorId = preferenceStorage.getUserId(); //idNumber

        Call<CreateSuccessfullyResponse> call = ServiceGenerator.getInstance().getApiConnector().medicalReportForm(
                symptoms,
                suspectedIllness,
                prescription,
                recommendation,
                patientID,
                doctorId
        );

        call.enqueue(new Callback<CreateSuccessfullyResponse>() {
            @Override
            public void onResponse(Call<CreateSuccessfullyResponse> call, Response<CreateSuccessfullyResponse> response) {
                if(response.code()==201 || response.code()==200){
                    Toast.makeText(OngoingAppointmentActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(OngoingAppointmentActivity.this, DoctorsSection.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(OngoingAppointmentActivity.this, "Something went wrong please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreateSuccessfullyResponse> call, Throwable t) {
                Log.d("MED_REPORT_FORM", t.getMessage());
                Toast.makeText(OngoingAppointmentActivity.this, "We encountered an technical issue, please try again later", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
package com.example.agapelife.instant_appointments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agapelife.R;
import com.example.agapelife.adapters.InstantMedicalReportsAdapter;
import com.example.agapelife.networking.pojos.InstantMedicalReport;
import com.example.agapelife.networking.services.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstantAppointmentActivity extends AppCompatActivity {
    TextView tvResponseMessageInstantAptmt, tvRegisteredFullname, tvRegisteredId, tvRegisteredPhone, tvRegisteredAge;
    RecyclerView rvPatientMedicalRecords;
    Button btnCreateInstantAppointment;

    Boolean patientCreated;
    int patientDbId;

    String fullName, phoneNumber;

    InstantMedicalReportsAdapter instantMedicalReportsAdapter;
    List<InstantMedicalReport> medicalReportList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_appointment);

        tvResponseMessageInstantAptmt = findViewById(R.id.tv_response_message_instant_aptmt);
        tvRegisteredFullname = findViewById(R.id.tv_registered_fullname);
        tvRegisteredId = findViewById(R.id.tv_registered_national_id);
        tvRegisteredPhone = findViewById(R.id.tv_registered_phone);
        tvRegisteredAge = findViewById(R.id.tv_registered_age);
        rvPatientMedicalRecords = findViewById(R.id.rv_patient_medical_records);
        btnCreateInstantAppointment = findViewById(R.id.btn_create_instant_appointment);

        rvPatientMedicalRecords.setNestedScrollingEnabled(true);
        rvPatientMedicalRecords.setLayoutManager(new LinearLayoutManager(InstantAppointmentActivity.this, LinearLayoutManager.VERTICAL, false));


        Intent intent = getIntent();

        phoneNumber = "Phone number: "+intent.getStringExtra("PHONE");
        fullName = "Full Name: "+intent.getStringExtra("FULL_NAME");

        patientCreated = intent.getBooleanExtra("PATIENT_CREATED", false);
        patientDbId = intent.getIntExtra("PATIENT_DB_ID", 0);
        tvResponseMessageInstantAptmt.setText(intent.getStringExtra("MESSAGE"));
        tvRegisteredFullname.setText(fullName);
        tvRegisteredId.setText("National ID: "+intent.getStringExtra("NATIONAL_ID"));
        tvRegisteredPhone.setText(phoneNumber);
        tvRegisteredAge.setText("Age: "+intent.getStringExtra("AGE"));

        btnCreateInstantAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InstantAppointmentActivity.this, OngoingAppointmentActivity.class);
                intent.putExtra("FULL_NAME", fullName);
                intent.putExtra("PHONE_NUMBER", phoneNumber);
                intent.putExtra("PATIENT_DB_ID", patientDbId);
                startActivity(intent);
            }
        });
        //        if this is not the first time the patient is being created
        if(!patientCreated){
            getPatientMedicalRecords();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getPatientMedicalRecords();
        instantMedicalReportsAdapter = new InstantMedicalReportsAdapter(
                                                medicalReportList,
                                                InstantAppointmentActivity.this
                                        );
        rvPatientMedicalRecords.setAdapter(instantMedicalReportsAdapter);
    }

    private void getPatientMedicalRecords() {
        Call<List<InstantMedicalReport>> call = ServiceGenerator.getInstance().getApiConnector().getPatientMedicalRecords(patientDbId);
        call.enqueue(new Callback<List<InstantMedicalReport>>() {
            @Override
            public void onResponse(Call<List<InstantMedicalReport>> call, Response<List<InstantMedicalReport>> response) {
                Log.d("patientMedCode", String.valueOf(response.code()));
                for(InstantMedicalReport report: response.body()){
                    Log.d("patientMedRecords", report.getPrescription());
                }
                Log.d("RESPONSE_CODE_MED_REC", String.valueOf(response.code()));
                if(response.code()==200){

                    medicalReportList.clear();
                    medicalReportList.addAll(response.body());
                    instantMedicalReportsAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(InstantAppointmentActivity.this, "There was problem fetching the medical records", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<InstantMedicalReport>> call, Throwable t) {
                Log.d("ERROR_MED_REC", t.getMessage());
                Toast.makeText(InstantAppointmentActivity.this, "We are encountering a technical problem please try again later", Toast.LENGTH_LONG).show();
            }
        });
    }
}
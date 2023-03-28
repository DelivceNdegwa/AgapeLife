package com.example.agapelife.instant_appointments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.agapelife.R;

public class InstantPrescriptionDetails extends AppCompatActivity {
    TextView tvPrescDetailsDateVal, tvPrescSuspectedIllnessVal, tvPrescPrescriptionVal, tvPrescRecommendationVal, cvPrescDrName, cvPrescHospital;

    int doctorID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_prescription_details);

        tvPrescDetailsDateVal = findViewById(R.id.tv_presc_details_date_val);
        tvPrescSuspectedIllnessVal = findViewById(R.id.tv_presc_details_illness_val);
        tvPrescPrescriptionVal = findViewById(R.id.tv_presc_details_prescription_val);
        tvPrescRecommendationVal = findViewById(R.id.tv_presc_details_recommendation_val);
        cvPrescDrName = findViewById(R.id.cv_presc_dr_name);
        cvPrescHospital = findViewById(R.id.cv_presc_hospital);

        Intent intent = getIntent();
        tvPrescDetailsDateVal.setText(intent.getStringExtra("PRESC_DATE"));
        tvPrescSuspectedIllnessVal.setText(intent.getStringExtra("SUSPECTED_ILLNESS"));
        tvPrescPrescriptionVal.setText(intent.getStringExtra("PRESCRIPTION"));
        tvPrescRecommendationVal.setText(intent.getStringExtra("RECOMMENDATION"));
        cvPrescHospital.setText(intent.getStringExtra("HOSPITAL")+" Hospital ");
        cvPrescDrName.setText("Dr. "+intent.getStringExtra("DOCTOR_NAME"));

        doctorID = intent.getIntExtra("DOCTOR_ID", 0);
    }
}


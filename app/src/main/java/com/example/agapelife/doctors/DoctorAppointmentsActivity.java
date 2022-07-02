package com.example.agapelife.doctors;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.agapelife.R;
import com.example.agapelife.utils.PreferenceStorage;

public class DoctorAppointmentsActivity extends AppCompatActivity {
    PreferenceStorage preferenceStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_appointments);

        preferenceStorage = new PreferenceStorage(this);
    }
}
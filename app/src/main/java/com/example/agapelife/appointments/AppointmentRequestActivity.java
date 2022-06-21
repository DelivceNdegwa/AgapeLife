package com.example.agapelife.appointments;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.agapelife.R;
import com.example.agapelife.UserMainActivity;
import com.example.agapelife.models.AppointmentRequest;
import com.example.agapelife.networking.services.ServiceGenerator;
import com.example.agapelife.utils.PreferenceStorage;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentRequestActivity extends AppCompatActivity {
    TextInputEditText etAbout, etSymptoms, etPeriod;
    Button btnSendRequest;
    PreferenceStorage preferenceStorage;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_request);

        preferenceStorage = new PreferenceStorage(this);
        progress = new ProgressDialog(this);

        etAbout = findViewById(R.id.et_about);
        etSymptoms = findViewById(R.id.et_symptoms);
        etPeriod = findViewById(R.id.et_period);
        btnSendRequest = findViewById(R.id.btn_send_request);

        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestAppointment();
                progress.setMessage("Sending request");
                progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                progress.show();

            }
        });
    }

    private void requestAppointment() {
        String about, symptoms, period;
        about = etAbout.getText().toString();
        symptoms = etSymptoms.getText().toString();
        period = etPeriod.getText().toString();
        long client_id = Long.parseLong(preferenceStorage.getUserId());
        long doctor_id = getDoctorId();
        Call<AppointmentRequest> call = ServiceGenerator.getInstance().getApiConnector().bookAppointment(
                client_id,
                doctor_id,
                about,
                symptoms,
                period
        );

        call.enqueue(new Callback<AppointmentRequest>() {
            @Override
            public void onResponse(Call<AppointmentRequest> call, Response<AppointmentRequest> response) {
                Log.d("onResponseAppointment", String.valueOf(response.body()));
                progress.hide();
                if(response.code() == 201){
                    Toast.makeText(AppointmentRequestActivity.this, "Your appointment request has been sent", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AppointmentRequestActivity.this, UserMainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(AppointmentRequestActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Log.d("onResponseAppointmentError", String.valueOf(response.errorBody().toString()));
                }
            }

            @Override
            public void onFailure(Call<AppointmentRequest> call, Throwable t) {
                Log.d("onFailure", String.valueOf(t.getStackTrace()));
                progress.hide();
                Toast.makeText(AppointmentRequestActivity.this, "Check your connection and try again", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private long getDoctorId() {
        Intent intent = getIntent();
        long id = intent.getLongExtra("DOCTOR_ID", 0);
        return id;
    }


}
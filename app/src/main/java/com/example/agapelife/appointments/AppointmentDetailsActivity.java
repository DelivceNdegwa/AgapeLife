package com.example.agapelife.appointments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agapelife.R;
import com.example.agapelife.networking.pojos.AgapeUserResponse;
import com.example.agapelife.networking.pojos.AppointmentResponse;
import com.example.agapelife.networking.services.ServiceGenerator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentDetailsActivity extends AppCompatActivity {

    ConstraintLayout dateContainer, timeContainer;
    TextView tvDate, tvTime, genderAge, patientName, appointmentTitle;
    String selectedDate="", start_time="", end_time, appointment_title, strPatientName;
//    Toolbar toolbar;
    
    long id;
    long patientId;

    AppointmentResponse appointmentResponse;
    AgapeUserResponse agapeUserResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);

//        toolbar = findViewById(R.id.custom_toolbar);
        appointmentTitle = findViewById(R.id.tv_appointment);
        patientName = findViewById(R.id.tv_doctor_details_name);
        genderAge = findViewById(R.id.tv_doctor_details_hospital);

        dateContainer = findViewById(R.id.date_container);
        timeContainer = findViewById(R.id.time_container);
        tvDate = findViewById(R.id.tv_date);
        tvTime = findViewById(R.id.tv_time);
    }

    @Override
    protected void onResume() {
        super.onResume();
        patientId = getIntent().getLongExtra("USER_ID", 0);
        id = getIntent().getLongExtra("APPOINTMENT_ID", 0);
        start_time = getIntent().getStringExtra("APPOINTMENT_TIME");
        selectedDate = getIntent().getStringExtra("APPOINTMENT_DATE");
        strPatientName = getIntent().getStringExtra("FULL_NAME");
        fetchAppointmentDetails(id);
        fetchPatientDetails(patientId);
    }

    private void fetchAppointmentDetails(long id) {
        Call<AppointmentResponse> call = ServiceGenerator.getInstance().getApiConnector().getAppointmentDetails(id);
        call.enqueue(new Callback<AppointmentResponse>() {
            @Override
            public void onResponse(Call<AppointmentResponse> call, Response<AppointmentResponse> response) {
                if(response.code() == 200 && response.body() != null){
                    Log.d("RESPONSE_BODY", String.valueOf(response.body()));
                    Log.d("RESPONSE_BODY_ID", String.valueOf(response.body().getId()));
                    Log.d("RESPONSE_BODY_ABT", response.body().getAbout());

                    appointmentResponse = response.body();
                    appointmentTitle.setText(appointmentResponse.getAbout());
                    tvDate.setText(selectedDate);
                    tvTime.setText(start_time);

                    try {
                        end_time = addEndTime(start_time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(AppointmentDetailsActivity.this, "Could not retrieve appointment details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AppointmentResponse> call, Throwable t) {

            }
        });
    }

    private void fetchPatientDetails(long patientId) {
        patientName.setText(strPatientName);
    }

    private String addEndTime(String inputTime) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date d = df. parse(inputTime);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar. MINUTE, 20);
        return df. format(cal.getTime());
    }
}
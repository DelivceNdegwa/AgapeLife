package com.example.agapelife.appointments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.example.agapelife.R;
import com.example.agapelife.doctors.DoctorsSection;
import com.example.agapelife.models.Appointment;
import com.example.agapelife.networking.services.ServiceGenerator;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAppointmentActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    ConstraintLayout dateContainer, timeContainer;
    TextView tvDate, tvTime, genderAge, patientName;
    String selectedDate="", start_time="", end_time, appointment_title;
    Toolbar toolbar;
    Button btnCreate;

    int doctor_id, client_id;

    TextInputEditText appointmentTitle;

    private int hour, minute;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_appointment);
        toolbar = findViewById(R.id.custom_toolbar);
        appointmentTitle = findViewById(R.id.appointment_title);
        patientName = findViewById(R.id.tv_patient_request_name);
        genderAge = findViewById(R.id.host_tag);

        dateContainer = findViewById(R.id.date_container);
        timeContainer = findViewById(R.id.time_container);
        tvDate = findViewById(R.id.tv_date);
        tvTime = findViewById(R.id.tv_time);

        btnCreate = findViewById(R.id.btn_create);

        dateContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDateCalendar();
            }
        });
        timeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayTimeClock();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appointment_title = appointmentTitle.getText().toString();
                verifyField(appointment_title, selectedDate, start_time);
            }
        });
        // title, starttime, endtime, doctor, client
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        client_id = intent.getIntExtra("CLIENT_ID",0);
        doctor_id = intent.getIntExtra("DOCTOR_ID", 0);
        patientName.setText(intent.getStringExtra("PATIENT_NAME"));

    }

    public void verifyField(String appointment_title, String setDate, String startTime){
        String color = "#FF8800", error_message;
        if(appointment_title.trim().isEmpty()){
            appointmentTitle.setError("Add appointment title");
            createAppointment();
        }

        else if(setDate.trim().isEmpty()){
            error_message = "Add appointment date";
            setErrorMessage(tvDate, color, error_message);

        }
        else if(startTime.trim().isEmpty()){
            error_message = "Please set an appointment time";
            setErrorMessage(tvTime, color, error_message);
        }
        else{
            createAppointment();
        }
    }

    private void createAppointment() {
        String startDateTime = selectedDate+" "+start_time;
        String endDateTime = selectedDate+" "+end_time;

        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            Date start_date_time = dateTimeFormatter.parse(startDateTime);
            Date end_date_time = dateTimeFormatter.parse(endDateTime);

//            Toast.makeText(this, String.valueOf(end_date_time), Toast.LENGTH_SHORT).show();

            Call<Appointment> call = ServiceGenerator.getInstance().getApiConnector().createAppointment(
                    appointment_title,
                    startDateTime,
                    endDateTime,
                    doctor_id,
                    client_id,
                    getIntent().getIntExtra("APPOINTMENT_ID", 0)
            );

            call.enqueue(new Callback<Appointment>() {
                @Override
                public void onResponse(Call<Appointment> call, Response<Appointment> response) {
                    Log.d("onResponseAppointment", String.valueOf(response.body()));
                    if(response.code() == 201){
                        Toast.makeText(CreateAppointmentActivity.this, "The appointment has been created", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(CreateAppointmentActivity.this, DoctorsSection.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(CreateAppointmentActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Appointment> call, Throwable t) {
                    Log.d("onFailure", String.valueOf(t.getStackTrace()));
                    Toast.makeText(CreateAppointmentActivity.this, "The appointment was NOT created", Toast.LENGTH_SHORT).show();
                }
            });


        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void setErrorMessage(TextView tvElement, String color, String message){
        tvElement.setTextColor(Color.parseColor(color));
        tvElement.setText(message);
    }

    private void displayTimeClock() {
        final Calendar c = Calendar.getInstance();

        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(CreateAppointmentActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                String AM_PM ;
                if(hour < 12) {
                    AM_PM = "AM";
                } else {
                    AM_PM = "PM";
                }

               start_time = String.valueOf(hour)+":"+ String.valueOf(min);
                SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                Date d = null;
                try {
                    d = df.parse(start_time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                cal.add(Calendar.MINUTE, 20);
                end_time = df.format(cal.getTime());


                tvTime.setText(start_time+" - "+end_time+AM_PM);
            }
        },hour,minute,true);

        timePickerDialog.show();
    }

    private void displayDateCalendar() {
        com.example.agapelife.utils.DatePicker mDatePickerDialogFragment;
        mDatePickerDialogFragment = new com.example.agapelife.utils.DatePicker();
        mDatePickerDialogFragment.show(getSupportFragmentManager(), "SET DATE");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String selected_date= DateFormat.getDateInstance(DateFormat.FULL).format(mCalendar.getTime());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(mCalendar.getTimeZone());
        selectedDate = dateFormat.format(mCalendar.getTime());

        tvDate.setText(selected_date);
    }
}
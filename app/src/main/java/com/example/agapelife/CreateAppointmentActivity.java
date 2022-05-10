package com.example.agapelife;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;


import java.text.DateFormat;
import java.util.Calendar;

public class CreateAppointmentActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    ConstraintLayout dateContainer, timeContainer;
    TextView tvDate, tvTime;
    String selectedDate;
    Toolbar toolbar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_appointment);
        toolbar = findViewById(R.id.custom_toolbar);

//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_circle_left_24);
//        actionBar.setDisplayHomeAsUpEnabled(true);
//
        dateContainer = findViewById(R.id.date_container);
        timeContainer = findViewById(R.id.time_container);
        tvDate = findViewById(R.id.tv_date);
        tvTime = findViewById(R.id.tv_time);
        
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
    }

    private void displayTimeClock() {

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
        selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(mCalendar.getTime());
        tvDate.setText(selectedDate);
    }
}
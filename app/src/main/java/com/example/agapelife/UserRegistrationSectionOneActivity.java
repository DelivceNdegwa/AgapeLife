package com.example.agapelife;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UserRegistrationSectionOneActivity extends AppCompatActivity {

    CardView maleDoctorCard, femaleDoctorCard;
    ConstraintLayout maleLayout, femaleLayout;
    TextView inputAge;

    ImageView imgDOBPicker;

    Button btnStepTwo;

    int gender, ageValue;
    String dateOfBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration_section_one);

        maleDoctorCard = findViewById(R.id.gender_male);
        femaleDoctorCard = findViewById(R.id.gender_female);
        maleLayout = findViewById(R.id.card_male_layout);
        femaleLayout = findViewById(R.id.card_female_layout);

        imgDOBPicker = findViewById(R.id.iv_dob_picker);
        inputAge = findViewById(R.id.input_age);

        btnStepTwo = findViewById(R.id.btn_step_two);

        maleDoctorCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maleLayout.setBackgroundResource(R.color.agape_primary);
                femaleLayout.setBackgroundColor(R.drawable.bg_gradient_two);
                gender = 1;
                Toast.makeText(UserRegistrationSectionOneActivity.this, "You have selected male", Toast.LENGTH_SHORT).show();
            }
        });

        femaleDoctorCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                femaleLayout.setBackgroundResource(R.color.agape_primary);
                femaleLayout.setBackgroundColor(R.drawable.bg_gradient_three);
                gender = 2;
                Toast.makeText(UserRegistrationSectionOneActivity.this, "You have selected female", Toast.LENGTH_SHORT).show();
            }
        });

        imgDOBPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDatePicker();
            }
        });

        btnStepTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserRegistrationSectionOneActivity.this, UserRegistrationSectionTwoActivity.class);
                startActivity(intent);
            }
        });
    }

    public void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month += 1;
                calculateDateOfBirth(year, month, day);
            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        new DatePickerDialog(this, style, dateSetListener, year, month, day).show();
    }

    private void calculateDateOfBirth(int year, int month, int day) {
        makeDateString(year, month, day);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate today = LocalDate.now();
            LocalDate birthday = LocalDate.of(year, month, day);

            Period period = Period.between(birthday, today);
            ageValue = period.getYears();
        }
    }

    private void makeDateString(int year, int month, int day) {
        String date = getMonthFormat(month)+" "+day+", "+year;
        dateOfBirth = year+"-"+month+"-"+day;
        inputAge.setText(date);
    }

    private String getMonthFormat(int month) {
        String [] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        int[] monthNumbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

        Map<Integer, String> months = IntStream.range(0, monthNumbers.length).boxed()
                .collect(Collectors.toMap(i -> monthNumbers[i], i -> monthNames[i]));

        return months.get(month);
    }

}
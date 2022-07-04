package com.example.agapelife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserRegistrationSectionTwoActivity extends AppCompatActivity {
    Button btnSectionThree, btnSectionOne;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration_section_two);

        btnSectionThree = findViewById(R.id.btn_step_three);
        btnSectionOne = findViewById(R.id.btn_step_one);

        btnSectionThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserRegistrationSectionTwoActivity.this, UserRegistrationSectionThreeActivity.class);
                startActivity(intent);
            }
        });

        btnSectionOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserRegistrationSectionTwoActivity.this, UserRegistrationSectionOneActivity.class);
                startActivity(i);
            }
        });
    }
}
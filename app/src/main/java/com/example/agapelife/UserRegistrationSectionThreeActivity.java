package com.example.agapelife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserRegistrationSectionThreeActivity extends AppCompatActivity {
    Button btnStepFour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration_section_three);

        btnStepFour = findViewById(R.id.btn_step_four);

        btnStepFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserRegistrationSectionThreeActivity.this, CreateAccountActivity.class);
                startActivity(intent);

            }
        });
    }
}
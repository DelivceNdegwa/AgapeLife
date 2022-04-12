package com.example.agapelife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class SignUpChoice extends AppCompatActivity {
    Button btnAgapeUser, btnAgapeDoctor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        setContentView(R.layout.activity_sign_up_choice);

        btnAgapeDoctor = findViewById(R.id.btn_doctor);
        btnAgapeUser = findViewById(R.id.btn_patient);

        btnAgapeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpChoice.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        btnAgapeDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUpChoice.this, DoctorsFormActivity.class);
                startActivity(i);
            }
        });
    }
}
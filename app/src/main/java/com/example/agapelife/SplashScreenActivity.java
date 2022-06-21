package com.example.agapelife;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.example.agapelife.appointments.VideoCallActivity;
import com.example.agapelife.authentication.LoginSignUpActivity;
import com.example.agapelife.utils.AnimationsConfig;
import com.example.agapelife.utils.GlobalVariables;
import com.example.agapelife.utils.PreferenceStorage;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 3500;
    ConstraintLayout mainLayout;
    PreferenceStorage preferenceStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        setContentView(R.layout.activity_splash_screen);

        mainLayout = findViewById(R.id.main_layout);
        preferenceStorage = new PreferenceStorage(this);

        AnimationsConfig mainLayoutAnimation = new AnimationsConfig();
        mainLayoutAnimation.createGradientAnimation(mainLayout, GlobalVariables.ENTER_FADE_DURATION, GlobalVariables.EXIT_FADE_DURATION);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if(preferenceStorage.isLoggedIn() == true){
                    if(preferenceStorage.isDoctor() == true){
//                        intent = new Intent(SplashScreenActivity.this, DoctorsSection.class);
                        intent = new Intent(SplashScreenActivity.this, VideoCallActivity.class);
                        startActivity(intent);
                        finish();
                    }
//                    else{
//                        intent = new Intent(SplashScreenActivity.this, UserMainActivity.class);
//                    }
                }
                else{
                    intent = new Intent(SplashScreenActivity.this, LoginSignUpActivity.class);
                    startActivity(intent);
                    finish();

                }
//                intent = new Intent(SplashScreenActivity.this, AgapeSocketActivityTest.class);

            }
        }, SPLASH_SCREEN);

    }
}
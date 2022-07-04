package com.example.agapelife;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.example.agapelife.appointments.VideoCallActivity;
import com.example.agapelife.authentication.LoginSignUpActivity;
import com.example.agapelife.doctors.DoctorsFormActivity;
import com.example.agapelife.doctors.DoctorsSection;
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

        addNotification("You have an appointment in 10 minutes", 2);

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
                        intent = new Intent(SplashScreenActivity.this, DoctorsSection.class);
//                        intent = new Intent(SplashScreenActivity.this, UserRegistrationSectionOneActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        intent = new Intent(SplashScreenActivity.this, UserMainActivity.class);
                    }
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

    public void addNotification(String message, int notificationType){
        //message="You have an appointment in the next 10 minutes"
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_add)
                .setContentTitle("Appointment Alert")
                .setAutoCancel(true)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        Intent notificationIntent = new Intent(this, DoctorsSection.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationIntent.putExtra("notification_message", message);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}
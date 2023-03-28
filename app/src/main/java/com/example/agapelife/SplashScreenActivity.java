package com.example.agapelife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.agapelife.appointments.VideoCallActivity;
import com.example.agapelife.authentication.LoginSignUpActivity;
import com.example.agapelife.doctors.DoctorsSection;
import com.example.agapelife.networking.pojos.FCMUserTokenOperations;
import com.example.agapelife.networking.services.ServiceGenerator;
import com.example.agapelife.utils.AnimationsConfig;
import com.example.agapelife.utils.GlobalVariables;
import com.example.agapelife.utils.PreferenceStorage;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//import com.pusher.pushnotifications.PushNotifications;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 3500;
    ConstraintLayout mainLayout;
    PreferenceStorage preferenceStorage;

    public static final String TAG = "SplashScreenActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        setContentView(R.layout.activity_splash_screen);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_HIGH));
        }


        mainLayout = findViewById(R.id.main_layout);
        preferenceStorage = new PreferenceStorage(this);


        Log.d("FCM_TOKEN-->", preferenceStorage.getFcmUserToken());
        getFCMToken();


        AnimationsConfig mainLayoutAnimation = new AnimationsConfig();
        mainLayoutAnimation.createGradientAnimation(mainLayout, GlobalVariables.ENTER_FADE_DURATION, GlobalVariables.EXIT_FADE_DURATION);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if(preferenceStorage.isLoggedIn()){
                    if(preferenceStorage.isDoctor()){
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
//                    intent = new Intent(SplashScreenActivity.this, VideoCallActivity.class);
                    startActivity(intent);
                    finish();

                }

            }
        }, SPLASH_SCREEN);
    }

    private void getFCMToken() {
        // Get token
        // [START log_reg_token]
        if(preferenceStorage.getFcmUserToken().isEmpty()){
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "Fetching FCM registration token failed", task.getException());

                                return;
                            }

                            // Get new FCM registration token
                            String token = task.getResult();

                            // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
                            Log.d(TAG, token);
                            preferenceStorage.setFcmUserToken(token);
                            Log.i("FCM_TOKEN-->", preferenceStorage.getFcmUserToken());
                            postFCMToken(preferenceStorage.getFcmUserToken());
                            Toast.makeText(SplashScreenActivity.this, token, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            Log.i("FCM_TOKEN-->", preferenceStorage.getFcmUserToken());
        }

        // [END log_reg_token]
    }

    private void postFCMToken(String fcmUserToken) {
        Call<FCMUserTokenOperations> call = ServiceGenerator.getInstance().getApiConnector().createOrUpdateToken(
                preferenceStorage.getUserId(),
                fcmUserToken
        );
        call.enqueue(new Callback<FCMUserTokenOperations>() {
            @Override
            public void onResponse(Call<FCMUserTokenOperations> call, Response<FCMUserTokenOperations> response) {
                if(response.body() != null && response.code() == 200){
                    Log.i(TAG, response.body().getSuccessMessage());

                }
                else if(response.code() == 400){
                    Log.e(TAG, response.body().getErrorMessage());

                }
            }

            @Override
            public void onFailure(Call<FCMUserTokenOperations> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

//    private void createPushNotification() {
//        PushNotifications.start(getApplicationContext(), "625de246-14e9-495a-af65-a11f8062d8d4");
//        PushNotifications.addDeviceInterest("hello");
//    }

}
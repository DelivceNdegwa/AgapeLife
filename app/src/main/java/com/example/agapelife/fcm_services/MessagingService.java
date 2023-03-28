package com.example.agapelife.fcm_services;

import static android.content.ContentValues.TAG;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.agapelife.R;
import com.example.agapelife.utils.PreferenceStorage;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.prefs.PreferenceChangeEvent;

public class MessagingService extends FirebaseMessagingService {

    public MessagingService() {
        Log.i("token ---->>>", "In waiting");
        FirebaseInstallations.getInstance().getId().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        String token = task.getResult();
                        Log.i("token ---->>", token);

                        // store the token in shared preferences
//                        PrefUtils.getInstance(getApplicationContext()).setValue(PrefKeys.FCM_TOKEN, token);

                    }
                }
        );
    }



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try {
            // whatever you want
            sendNotification(remoteMessage);
        } catch (Exception e) {
            // whatever you want
            e.printStackTrace();
        }
    }

    @Override
    public void onNewToken(@NotNull String token) {
        super.onNewToken(token);
        // whatever you want
        Log.i("token ---->", token);
        updateFCMNotification(token);
    }

    private void updateFCMNotification(String token) {
        PreferenceStorage preferenceStorage = new PreferenceStorage(this);
        preferenceStorage.setFcmUserToken(token);
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String NOTIFICATION_CHANNEL_ID = getString(R.string.default_notification_channel_id);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Your Notifications",
                    NotificationManager.IMPORTANCE_HIGH);

            long pattern[] = {0, 1000, 500, 1000};


            notificationChannel.setDescription("");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.setVibrationPattern(pattern);
            notificationChannel.enableVibration(true);
            mNotificationManager.createNotificationChannel(notificationChannel);

        }

        // to diaplay notification in DND Mode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = mNotificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID);
            channel.canBypassDnd();
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notificationBuilder.setAutoCancel(true)
                .setColor(ContextCompat.getColor(this, R.color.agape_primary))
                .setContentTitle(getString(R.string.app_name))
                .setContentText(remoteMessage.getNotification().getBody())
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setSmallIcon(R.drawable.ic_baseline_add_alert_24)
                .setAutoCancel(true);

//        mNotificationManager.notify(1000, notificationBuilder.build());
        mNotificationManager.notify(0, notificationBuilder.build());

    }
}

package com.example.agapelife.utils;

import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationBuilder {
    Context context;
    String channelId, contentTitle, contentMessage;
    int smallIcon, NOTIFICATION_PRIORITY;

    public NotificationBuilder(Context context, String channelId, String contentTitle, String contentMessage, int smallIcon, int NOTIFICATION_PRIORITY) {
        this.context = context;
        this.channelId = channelId;
        this.contentTitle = contentTitle;
        this.contentMessage = contentMessage;
        this.smallIcon = smallIcon;
        this.NOTIFICATION_PRIORITY = NOTIFICATION_PRIORITY;
    }

//
//    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//
//    }

    public void buildNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);
        builder.setSmallIcon(smallIcon);
        builder.setContentTitle(contentTitle);
        builder.setContentText(contentMessage);
        builder.setAutoCancel(false);
        builder.setPriority(NOTIFICATION_PRIORITY);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(12, builder.build());
    }
}

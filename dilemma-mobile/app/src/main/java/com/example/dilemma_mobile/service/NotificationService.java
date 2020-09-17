package com.example.dilemma_mobile.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.dilemma_mobile.R;

import static com.example.dilemma_mobile.activities.ChannelNotification.CHANNEL_1_ID;

public class NotificationService extends Service {

    public void createNotification(String title, String message, Context context,Object notificationservice){
        NotificationManager notificationManager = (NotificationManager) notificationservice;
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

package com.example.nicko.turuntanganmalangapps.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.nicko.turuntanganmalangapps.MainActivity;
import com.example.nicko.turuntanganmalangapps.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by nicko on 7/1/2017.
 */

public class FcmMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        String title = remoteMessage.getNotification().getTitle();
//        String message = remoteMessage.getNotification().getBody();

        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("body");

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(message);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());

        super.onMessageReceived(remoteMessage);
    }
}

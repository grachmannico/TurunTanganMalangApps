package com.example.nicko.turuntanganmalangapps.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.nicko.turuntanganmalangapps.MainActivity;
import com.example.nicko.turuntanganmalangapps.R;
import com.example.nicko.turuntanganmalangapps.activities.DetailKegiatanActivity;
import com.example.nicko.turuntanganmalangapps.parser.JSONParser;
import com.example.nicko.turuntanganmalangapps.sqlite.NotificationModel;
import com.example.nicko.turuntanganmalangapps.sqlite.SQLiteHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by nicko on 7/1/2017.
 */

public class FcmMessagingService extends FirebaseMessagingService {

    private NotificationModel notifModel;
    private SQLiteHelper sqLiteHelper;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String message = remoteMessage.getData().get("message");
        String message_type = remoteMessage.getData().get("messagetype");
        String intent = remoteMessage.getData().get("intent");
        String id_target = remoteMessage.getData().get("idtarget");
        String date_rcv = remoteMessage.getData().get("datercv");

//        Intent start_intent = new Intent(this, MainActivity.class);
//        start_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, start_intent, PendingIntent.FLAG_ONE_SHOT);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
//        notificationBuilder.setContentTitle(title);
//        notificationBuilder.setContentText(message_type);
//        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
//        notificationBuilder.setAutoCancel(true);
//        notificationBuilder.setContentIntent(pendingIntent);
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(0, notificationBuilder.build());
//        super.onMessageReceived(remoteMessage);

        PendingIntent pendingIntent = null;
        Intent startIntent = null;

        if (intent.equals("DetailKegiatanActivity")) {
            notifModel = new NotificationModel();
            notifModel.setTitle(title);
            notifModel.setBody(body);
            notifModel.setMessage(message);
            notifModel.setMessage_type(message_type);
            notifModel.setIntent(intent);
            notifModel.setId_target(id_target);
            notifModel.setDate_rcv(date_rcv);
            sqLiteHelper = new SQLiteHelper(this);
            sqLiteHelper.insertRecord(notifModel);
            startIntent = new Intent(this, DetailKegiatanActivity.class);
            startIntent.putExtra("id_kegiatan", id_target);

            pendingIntent = PendingIntent.getActivity(this, 69, startIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification nf = new Notification.Builder(this)
                    .setContentTitle(title)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText(body)
                    .setContentIntent(pendingIntent)
                    .build();
            nf.flags = Notification.FLAG_NO_CLEAR;
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.notify(0, nf);
        } else if (intent.equals("transaksi")) {

        }

        new JSONParser(getApplicationContext());
    }
}

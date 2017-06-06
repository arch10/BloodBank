package com.gigaworks.bloodbankbeta;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by Arch on 31-03-2017.
 */

public class MessagingService extends FirebaseMessagingService {

    final static String TAG="Archit";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        String text="";

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
        }
        
        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            Intent i=new Intent(this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent= PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle(getString(R.string.app_name));
        notificationBuilder.setContentText(remoteMessage.getNotification().getBody());
        notificationBuilder.setSmallIcon(R.drawable.ic_stat_ic_notification);
        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_stat_ic_notification));
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE);
        NotificationManager notificationManager= (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());

        }
    }


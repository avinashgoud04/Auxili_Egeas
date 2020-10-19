package com.example.auxili_egeas.Notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.auxili_egeas.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String title,message;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        title = remoteMessage.getData().get("Title");
        message = remoteMessage.getData().get("Message");


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            OreoNotification oreoNotification=new OreoNotification(this);

            Notification.Builder builder=oreoNotification.getOreoNotification(title,message,RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


            manager.notify(0, builder.build());

        }
        else {

            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.ic_baseline_motorcycle_24)
                            .setContentTitle(title)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setContentText(message);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


            manager.notify(0, builder.build());
        }

    }
}

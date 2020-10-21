package com.example.auxili_egeas.Notifications;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.example.auxili_egeas.Adapter.ChatAdapter;
import com.example.auxili_egeas.ChatActivity;
import com.example.auxili_egeas.Fragments.RentFragment;
import com.example.auxili_egeas.MainActivity;
import com.example.auxili_egeas.R;

public class OreoNotification extends ContextWrapper {

    private static final String CHANNEL_ID="com.example.auxili_egeas";
    private static final String CHANNEL_NAME="auxili_egeas";

    private NotificationManager notificationManager;

    public OreoNotification(Context base) {
        super(base);


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            createChannel();
        }

    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel(){

        NotificationChannel channel=new NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT);

        channel.enableLights(false);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel);
    }


    public NotificationManager getManager(){

        if(notificationManager==null)
        {
            notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getOreoNotification(String title, String body, Uri soundUri)
    {
        Intent intent; PendingIntent pendingIntent;
        if(title.equals("New Message"))
        {
            intent=new Intent(this, MainActivity.class);
            pendingIntent=PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        }
        else {
            intent = new Intent(this, MainActivity.class);
            pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);
        }

        return new Notification.Builder(getApplicationContext(),CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(soundUri)
                .setSmallIcon(R.drawable.ic_baseline_motorcycle_24)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

    }

}

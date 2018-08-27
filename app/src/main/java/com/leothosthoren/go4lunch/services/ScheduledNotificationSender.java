package com.leothosthoren.go4lunch.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.controler.activities.RestaurantInfoActivity;

import java.util.Objects;


public class ScheduledNotificationSender extends BroadcastReceiver {

    // VAR
    private String subText = "test"; // Info from firebase ...

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
            // todo:
            // - check if notification enabled
            // - check if restaurant choice exists on the current day

            // - launch notification
            sendNotification(context);
        }

    }

    /*
     * @method sendNotification
     * @param context
     *
     * Notify the user even the app is not launched
     * */
    public void sendNotification(Context context) {

        final int NOTIFICATION_ID = 700;
        String channelID = context.getString(R.string.default_notification_channel_id);
        Intent intent = new Intent(context, RestaurantInfoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        //Get an instance of NotificationManager//

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, channelID)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText(context.getString(R.string.text_notification))
                        .setSubText(subText)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

        // Gets an instance of the NotificationManager service//

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //  Support Version >= Android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Message";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelID, channelName, importance);
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(mChannel);
        }

        assert mNotificationManager != null;
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}

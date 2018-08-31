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
import com.leothosthoren.go4lunch.api.RestaurantHelper;
import com.leothosthoren.go4lunch.controler.activities.RestaurantInfoActivity;
import com.leothosthoren.go4lunch.data.DataSingleton;
import com.leothosthoren.go4lunch.model.detail.PlaceDetail;
import com.leothosthoren.go4lunch.model.firebase.Restaurants;
import com.leothosthoren.go4lunch.utils.DataConverterHelper;
import com.leothosthoren.go4lunch.utils.FireBaseTools;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


public class ScheduledNotificationSender extends BroadcastReceiver implements FireBaseTools, DataConverterHelper {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        this.getDataFromFirestore(context);
        if (Objects.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED")) {
            // First the data is update and then the notification is send
            this.getDataFromFirestore(context);
        }

    }


    /**
     * @param context Notify the user even the app is not launched
     * @method sendNotification
     */
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
                        .setContentText(context.getString(R.string.text_notification, Objects.requireNonNull(getCurrentUser()).getDisplayName()))
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


    /**
     * @param context
     * @method getDataFromFirestore
     *
     * Get the data from Firestore and check several conditions before sending
     */
    public void getDataFromFirestore(Context context) {
        // Get Restaurant selection from Firestore
        if (getCurrentUser() != null) {
            RestaurantHelper.getRestaurantSelection(getCurrentUser().getUid())
                    .addOnSuccessListener(documentSnapshot -> {
                        Restaurants restaurants = documentSnapshot.toObject(Restaurants.class);
                        if (restaurants != null) {
                            Date dateChoice = restaurants.getDateChoice();
                            PlaceDetail placeDetail = restaurants.getPlaceDetail();
                            boolean notification = restaurants.getWorkmate().getNotificationEnabled();
                            if (notification && formatDate(dateChoice).equals(formatDate(Calendar.getInstance().getTime()))) {
                                //Set Singleton
                                DataSingleton.getInstance().setPlaceDetail(placeDetail);
                                // Send Notification
                                this.sendNotification(context);
                            }
                        }
                    }).addOnFailureListener(this.onFailureListener(context));
        }

    }

}

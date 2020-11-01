package com.rs.doorbellvendor.FCM;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rs.doorbellvendor.MainActivity;
import com.rs.doorbellvendor.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    String message, title, tag, rideRequestId, sourceLongitude, sourceLatitude, destinationLatitude, destlongi, rideRequestIdnew = "";
    //    static Messagee messalistner;
    NotificationManager notificationManager;
     Intent intent;
    //  CustomerNewRequestModel customerNewRequestModel;
    public static String cancl;

    private NotificationCompat.Builder notificationBuilder;
    private int currentNotificationID = 0;


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN",s);
    }


    @Override
    public void onSendError(@NonNull String s, @NonNull Exception e) {
        super.onSendError(s, e);
        Log.e("data",s);
    }

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {


        Log.e("notif dle------/", "From: " + remoteMessage.getFrom());

        Log.e(TAG, "Message data payload: " + remoteMessage.getData());

//            String noti = remoteMessage.getNotification().getBody();

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());
            Log.e("remoteMessage:: " + remoteMessage, TAG);
            Log.e("data:: " + remoteMessage.getData(), TAG);
            Map<String, String> data = remoteMessage.getData();
            Log.e("Map data:: " + data, TAG);
            setDataForSimpleNotification(data);
        }
    }

    private void setDataForSimpleNotification(Map<String, String> data) {
        JSONObject jsonObject = new JSONObject(data);
        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        try {
            String url = "http://ozzyfood.online/restaurant_panel/"+jsonObject.get("url").toString();
            String title = jsonObject.get("title").toString();
            String body = jsonObject.get("body").toString();

                Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.logo);
                notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setLargeIcon(icon)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setChannelId("my_channel_01");

                Intent notificationIntent = new Intent(this, MainActivity.class);
                notificationIntent.putExtra("notification",true);
                notificationIntent.putExtra("url",url);
             // notificationIntent.putExtra("message",chat.getMessage());
              //PendingIntent  contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                PendingIntent contentIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), notificationIntent, 0);
                notificationBuilder.setContentIntent(contentIntent);
                sendNotification();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void sendNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel("my_channel_01", "MziGo", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }

        Notification notification = notificationBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        currentNotificationID++;
        int notificationId = currentNotificationID;
        if (notificationId == Integer.MAX_VALUE - 1)
            notificationId = 0;
        notificationManager.notify(1, notification);
    }

}
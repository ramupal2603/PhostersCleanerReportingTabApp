package com.brinfotech.feedbacksystem.firebasePushNotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.Log;

import com.brinfotech.feedbacksystem.R;

import com.brinfotech.feedbacksystem.ui.qrCodeScannerView.QrCodeScannerViewActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


public class MyFireBaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFireBaseMessagingService.class.getSimpleName();
    public static int NOTIFICATION_ID = 5000;

    String msg;
    String pageType;
    String itemId;


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void generateNotification(Context context, String title,
                                            String message, Intent intent, int id) {

        NOTIFICATION_ID = random(0, NOTIFICATION_ID);

        int icon = R.drawable.launcher_icon;

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(NOTIFICATION_SERVICE);

        PendingIntent pendingIntent = null;

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                icon);


        if (intent != null) {

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);

            int iUniqueId = (int) (System.currentTimeMillis() & 0xfffffff);

            pendingIntent = PendingIntent.getActivity(context, iUniqueId,
                    intent, PendingIntent.FLAG_CANCEL_CURRENT);


        }
        String notificationMode = "";
        Notification.Builder notificationBuilder = new Notification.Builder(
                context).setContentTitle("" + "Notification").setSmallIcon(icon)
                .setLargeIcon(bitmap).setTicker("" + title)
                .setContentText("" + message).setAutoCancel(true);

        //notificationMode = Prefs.getString(PrefsKeys.NOTIFICATION_MODE, "");
        if (notificationMode.isEmpty()) {
            notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);

        }

        if (intent != null) {
            notificationBuilder.setContentIntent(pendingIntent);
        }

        notificationBuilder.setStyle(new Notification.BigTextStyle()
                .setBigContentTitle("" + title).bigText("" + message));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(Color.parseColor("#000000"));
        }

        Notification notification = notificationBuilder.build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channelId = context.getPackageName();

            notificationBuilder.setChannelId(channelId);

            CharSequence name = "JLL Contractor Solution";
            String description = "Notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel mChannel = new NotificationChannel(channelId, name, importance);

            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.parseColor("#000000"));

            mChannel.enableVibration(true);
            notificationManager.createNotificationChannel(mChannel);

        }
        notificationManager.notify(id, notification);

    }

    public static int random(int min, int max) {

        int value = Math
                .round((float) (min + (Math.random() * ((max - min) + 1))));

        if (value > max) {
            return max;
        } else if (value < min) {
            return min;
        } else {
            return value;
        }

    }


    //----------------------- other methods ------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e(TAG, "From: " + remoteMessage.getFrom());

        Map<String, String> data = remoteMessage.getData();

        /**
         * Fcm Sends notification then retireve from here.
         */
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            msg = remoteMessage.getNotification().getBody();
//            pageType = data.get("page_type");
//            itemId = data.get("item_id");
        }

        Intent intent = new Intent(this, QrCodeScannerViewActivity.class);
        generateNotification(this, "Notifications", msg, intent, NOTIFICATION_ID);
    }




}

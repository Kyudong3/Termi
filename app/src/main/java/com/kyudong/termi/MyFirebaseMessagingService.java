package com.kyudong.termi;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Kyudong on 16. 11. 21..
 */
///////////////////FCM 서버에서 메시지를 받은 후 처리하는 소스/////////////////////
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    String imgsrc = "";
    private String dataStr;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //sendNotification(remoteMessage.getData().get("message"));
        //Log.e(TAG, "From: " + remoteMessage.getFrom());

        if(remoteMessage.getData().size() > 0) {
            if(remoteMessage.getData().get("type").equals("1")){
                dataStr = "보냈던 쪽지가 상대방에게 도착했어요!";
            }
            else if(remoteMessage.getData().get("type").equals("0")){
                dataStr = "누군가 보낸 쪽지가 드디어 도착했어요!";
            }
            //Log.e(TAG, "Message data payload: " + remoteMessage.getData());
        }

        if(remoteMessage.getNotification() != null) {
            //Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        //imgsrc = remoteMessage.getData().get("imgsrc");
        sendNotification(dataStr);
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent, PendingIntent.FLAG_ONE_SHOT);

        if(messageBody != null) {
            //Log.d(TAG, messageBody);
        }


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        android.support.v4.app.NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon))
                .setColor(getResources().getColor(R.color.txttitle))
                .setContentTitle(dataStr)
                .setContentText("지금 바로 확인하세요")
                .setTicker("Termi")
//                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());



//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentTitle(messageBody)
//                .setContentIntent(pendingIntent);
    }
}

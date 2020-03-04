package com.jtmcompany.waist_guard_project.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.jtmcompany.waist_guard_project.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "TAKMIN";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            String body = remoteMessage.getNotification().getBody();
            Log.d(TAG, "Notification Body" + body);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "default")
                    .setSmallIcon(R.mipmap.ic_launcher) // 알림 영역에 노출 될 아이콘.
                    .setContentTitle("친구요청") // 알림 영역에 노출 될 타이틀
                    .setContentText(body); // Firebase Console 에서 사용자가 전달한 메시지내용

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
            notificationManagerCompat.notify(0x1001, notificationBuilder.build());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                manager.createNotificationChannel(new NotificationChannel("default","기본 채널",NotificationManager.IMPORTANCE_DEFAULT));
            }

        }

    }

}

package com.labawsrh.aws.trashtec.Interfaces;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.labawsrh.aws.trashtec.Activitys.IntroActivity;
import com.labawsrh.aws.trashtec.Activitys.Main_User_Activity;
import com.labawsrh.aws.trashtec.Clases_Helper.Firebase_Variables;
import com.labawsrh.aws.trashtec.R;

public class NotificacionMessageService extends FirebaseMessagingService {
    private FirebaseAuth auth = Firebase_Variables.firebaseauth;
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        String Title = notification.getTitle();
        String mensaje = notification.getBody();
        sendNotification(Title,mensaje);
    }

    private void sendNotification(String title, String mensaje) {
        Intent intent;
        if(auth.getCurrentUser()!=null)
            intent =  new Intent(this, Main_User_Activity.class);
        else
            intent = new Intent(this,IntroActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,MyNotification.NOTIFICATION_ID,intent,PendingIntent.FLAG_ONE_SHOT);
        MyNotification notification = new MyNotification(this,MyNotification.CHANNEL_ID_NOTIFICATIONS);
        notification.build(R.drawable.icon_basura,title,mensaje,pendingIntent);
        notification.addChannel("Notificaciones ", NotificationManager.IMPORTANCE_DEFAULT);
        notification.createChannelGroup(MyNotification.CHANNEL_GROUP_GENERAL,R.string.group);
        notification.show(MyNotification.NOTIFICATION_ID);

    }
}

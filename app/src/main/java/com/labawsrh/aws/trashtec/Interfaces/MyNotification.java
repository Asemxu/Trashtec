package com.labawsrh.aws.trashtec.Interfaces;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.media.AudioAttributes;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.labawsrh.aws.trashtec.R;

public class MyNotification {
        public static final String CHANNEL_ID_NOTIFICATIONS = "channel_id_notifications";
        public static final String CHANNEL_GROUP_GENERAL = "channel_group_general";
        public static final int NOTIFICATION_ID = 1;

        private NotificationCompat.Builder notificationBuilder;
        private NotificationManager notificationManager;
        private NotificationChannel channel;
        private Context context;
        private String channelId;

        public MyNotification(Context context, String channelId) {
            this.notificationBuilder = new NotificationCompat.Builder(context, channelId);
            this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            this.context = context;
            this.channelId = channelId;
        }

        public void createChannelGroup(String groupId, int groupNameId){
            CharSequence groupName = context.getString(groupNameId);
            notificationManager.createNotificationChannelGroup(new NotificationChannelGroup(groupId, groupName));
            channel.setGroup(groupId);
        }

        public void build(int imgId, String title, String content, PendingIntent pendingIntent){
            notificationBuilder.setSmallIcon(imgId)
                    .setColor(context.getResources().getColor(R.color.colorAccent))
                    .setContentTitle(title)
                    .setContentText(content)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(content));
        }

        public void addChannel(CharSequence chanelName, int importance){
            channel = new NotificationChannel(channelId, chanelName, importance);
        }

        public void setVibrate(){
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000});
        }

        public void setSound(Uri sound, boolean set) {
            if (!set)
                return;
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .build();
            channel.setSound(sound, audioAttributes);
        }

        public void show(int idAlert){
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(idAlert, notificationBuilder.build());
        }
    }

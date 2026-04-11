package dev.omar.nettyproxy.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import dev.omar.nettyproxy.R;

public final class NotificationHelper {

    public static void createNotificationChannel(
            Context context, String channelId, int importance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel =
                    new NotificationChannel(
                            channelId, context.getString(R.string.app_name), importance);
            NotificationManager manager = getNotificationManager(context);
            if (manager != null && (manager.getNotificationChannel(channelId)==null)) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    public static NotificationManager getNotificationManager(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getSystemService(NotificationManager.class);
        } else {
            return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
    }
}

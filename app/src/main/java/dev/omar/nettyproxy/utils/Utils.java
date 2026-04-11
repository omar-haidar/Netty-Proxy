package dev.omar.nettyproxy.utils;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.StatusBarManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.IconCompat;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import dev.omar.nettyproxy.MainActivity;
import dev.omar.nettyproxy.R;
import dev.omar.nettyproxy.services.tile.ProxyTileService;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.Executors;

public final class Utils {
    public static final int REQ_POST_NOTIFICATION = 2002;

    public static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (!addr.isLoopbackAddress() && addr instanceof Inet4Address) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
        }
        return "127.0.0.1";
    }

    public static void showGrantNotificationPermissionDialog(Activity activity) {
        MaterialAlertDialogBuilder d = new MaterialAlertDialogBuilder(activity);
        d.setTitle("Access to notifications is required!");
        d.setMessage(
                "Please grant access permissions for notifications to ensure the service continues to run in the background.");
        d.setPositiveButton(
                "Grant",
                (_d, _i) -> {
                    ActivityCompat.requestPermissions(
                            activity,
                            new String[] {Manifest.permission.POST_NOTIFICATIONS},
                            REQ_POST_NOTIFICATION);
                });
        d.setNegativeButton("Exit", (_d, _i) -> activity.finishAffinity());
        d.setCancelable(false);
        d.show();
    }

    public static int getPendingIntentFlags() {
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        } else {
            flags |= PendingIntent.FLAG_ONE_SHOT;
        }
        return flags;
    }

    public static boolean isIgnoringBatteryOptimizations(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager manager = context.getSystemService(PowerManager.class);
            return manager.isIgnoringBatteryOptimizations(context.getPackageName());
        }
        return true;
    }

    public static boolean hasNotificationPermissions(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(
                            context, Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public static int getColorByStatus(Context context, boolean flag) {
        return flag
                ? ContextCompat.getColor(context, R.color.success)
                : ContextCompat.getColor(context, R.color.error);
    }
}

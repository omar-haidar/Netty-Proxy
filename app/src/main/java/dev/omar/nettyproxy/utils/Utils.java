package dev.omar.nettyproxy.utils;

import android.Manifest;
import android.app.Activity;
import androidx.core.app.ActivityCompat;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import dev.omar.nettyproxy.MainActivity;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

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
}

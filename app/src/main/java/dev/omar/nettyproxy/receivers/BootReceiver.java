package dev.omar.nettyproxy.receivers;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import dev.omar.nettyproxy.proxy.ProxyController;
import dev.omar.nettyproxy.utils.Settings;

public class BootReceiver extends BroadcastReceiver {
    private Settings mSettings;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            try {
                mSettings = new Settings(context);
                if (mSettings.isStartupWithBoot()) {
                    ProxyController.getInstance().startProxyService(context);
                }
            } catch (Exception err) {

            }
        }
    }
}

package dev.omar.nettyproxy.receivers;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import dev.omar.nettyproxy.proxy.ProxyController;

public class ControllerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        switch(intent.getAction()){
            case ProxyController.ACTION_STOP_PROXY:
            ProxyController.getInstance().stopProxyService(context);
            break;
        }
        
    }
}

package dev.omar.nettyproxy.services.tile;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import dev.omar.nettyproxy.MainActivity;
import dev.omar.nettyproxy.R;
import dev.omar.nettyproxy.proxy.ProxyController;
import dev.omar.nettyproxy.utils.Utils;

@TargetApi(24)
public class ProxyTileService extends TileService implements Observer<Boolean> {

    @Override
    public void onCreate() {
        super.onCreate();
        ProxyController.getInstance().isRunningProxyService().observeForever(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ProxyController.getInstance().isRunningProxyService().removeObserver(this);
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        updateTileState();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        updateTileState();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
        updateTileState();
    }

    @Override
    public void onClick() {
        super.onClick();
        if (Utils.hasNotificationPermissions(this)) {
            ProxyController.getInstance().toggleProxyServiceRunning(this);
            updateTileState();
        } else {
            startActivityAndCollapseCompat(new Intent(this,MainActivity.class));
        }
    }
    
    @SuppressWarnings({"StartActivityAndCollapseDeprecated","deprecation"})
    private void startActivityAndCollapseCompat(Intent intent) {
        if(Build.VERSION.SDK_INT>=34) {
        	startActivityAndCollapse(PendingIntent.getActivity(getApplicationContext(),3003,intent,Utils.getPendingIntentFlags()));
        } else {
        	startActivityAndCollapse(intent);
        }
    }

    private void updateTileState() {
        Tile tile = getQsTile();
        if (tile == null) return;

        if (ProxyController.getInstance().isRunningProxyService().getValue()) {
            tile.setState(Tile.STATE_ACTIVE);
        } else {
            tile.setState(Tile.STATE_INACTIVE);
        }
        tile.updateTile();
    }

    @Override
    public void onChanged(@NonNull Boolean isRunning) {
        new Handler(Looper.getMainLooper())
                .postDelayed(
                        () -> {
                            updateTileState();
                        },
                        250); 
    }
}

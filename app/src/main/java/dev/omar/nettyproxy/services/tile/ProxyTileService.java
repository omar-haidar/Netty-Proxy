package dev.omar.nettyproxy.services.tile;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Icon;
import android.os.Handler;
import android.os.Looper;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import dev.omar.nettyproxy.R;
import dev.omar.nettyproxy.proxy.ProxyController;

@TargetApi(24)
public class ProxyTileService extends TileService implements Observer<Boolean>{

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
        ProxyController.getInstance().toggleProxyServiceRunning(this);
        updateTileState();
    }
    
    private void updateTileState() {
        Tile tile = getQsTile();
        if (tile == null) return;
        
        if (ProxyController.getInstance().isRunningProxyService().getValue()) {
            tile.setState(Tile.STATE_ACTIVE);
            tile.setLabel("Proxy Active");
        } else {
            tile.setState(Tile.STATE_INACTIVE);
            tile.setLabel("Proxy not active");
        }
        tile.updateTile();
    }
    
    @Override
    public void onChanged(@NonNull Boolean isRunning) {
        new Handler(Looper.getMainLooper()).postDelayed(()->{
            updateTileState();
        },250);
    }
    
}
package dev.omar.nettyproxy.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;

import androidx.core.app.NotificationCompat;

import dev.omar.nettyproxy.MainActivity;
import dev.omar.nettyproxy.R;
import dev.omar.nettyproxy.proxy.ProxyController;
import dev.omar.nettyproxy.proxy.api.IProxyServer;
import dev.omar.nettyproxy.proxy.netty.HttpProxyChannelInitializer;
import dev.omar.nettyproxy.proxy.netty.HttpProxyClientHandler;
import dev.omar.nettyproxy.proxy.netty.HttpProxyClientHandlerFactory;
import dev.omar.nettyproxy.proxy.netty.HttpProxyClientHeader;
import dev.omar.nettyproxy.proxy.netty.HttpProxyServer;

import dev.omar.nettyproxy.utils.NotificationHelper;
import dev.omar.nettyproxy.utils.Utils;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.util.concurrent.atomic.AtomicLong;

public class ProxyService extends BaseService {

    private static final String CHANNEL_ID = "NettyProxyChannel";
    private static final int NOTIFICATION_ID = 1001;

    private IProxyServer proxyServer;
    
    private final AtomicLong taskCounter = new AtomicLong(0);
    
    private PowerManager.WakeLock wakeLock;

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationHelper.createNotificationChannel(this,CHANNEL_ID,NotificationManager.IMPORTANCE_LOW);
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock =
                powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ProxyService::WakeLock");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case ProxyController.ACTION_START_PROXY:
                    startProxy();
                    break;
                case ProxyController.ACTION_STOP_PROXY:
                    stopProxy();
                    break;
                default:
                    startProxy();
                    break;
            }
        } else {
            startProxy();
        }
        return START_STICKY;
    }

    private void startProxy() {
        try {
            wakeLock.acquire();
            HttpProxyClientHandlerFactory factory =
                    taskId -> {
                        HttpProxyClientHeader header = new HttpProxyClientHeader();
                        return new HttpProxyClientHandler("task-" + taskId, header);
                    };

            ChannelInitializer<SocketChannel> initializer =
                    new HttpProxyChannelInitializer(taskCounter, factory);

            proxyServer = new HttpProxyServer(initializer);
            proxyServer.startProxy();

            showForegroundNotification();

        } catch (Exception e) {
            stopSelf();
        }
    }

    private void showForegroundNotification() {
        String ip = proxyServer.getIP();

        String contentText = "Running proxy : " + ip + ":" + proxyServer.getPort();
        
        Intent mainActivityIntent = new Intent(this,MainActivity.class);
        mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Notification notification =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("Netty Proxy active")
                        .setContentText(contentText)
                        .setSmallIcon(R.drawable.ic_hub)
                        .setOngoing(true)
                        .setContentIntent(PendingIntent.getActivity(this,33,mainActivityIntent,Utils.getPendingIntentFlags()))
                        .build();

        startForeground(NOTIFICATION_ID, notification);
    }
    
    @Override
    public void onDestroy() {
        stopProxy();
        super.onDestroy();
    }

    @SuppressWarnings("deprecation")
    private void stopProxy() {
        if (proxyServer != null && proxyServer.isRunningProxy()) {
            proxyServer.stopProxy();
            proxyServer = null; 
        }
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
        stopForeground(true);
    }

    
}

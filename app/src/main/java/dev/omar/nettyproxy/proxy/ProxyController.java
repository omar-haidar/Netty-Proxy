package dev.omar.nettyproxy.proxy;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import dev.omar.nettyproxy.proxy.api.IProxyServer;
import dev.omar.nettyproxy.services.ProxyService;

public final class ProxyController {
    public static final String ACTION_START_PROXY = "action:proxy:start";
    public static final String ACTION_STOP_PROXY = "action:proxy:stop";

    private ProxyController() {}

    private static final ProxyController INSTANCE = new ProxyController();

    private static MutableLiveData<Boolean> isRunningProxyService =
            new MutableLiveData<Boolean>(false);
private static MutableLiveData<ProxyServerProvider> proxyServerProviderLiveData =
            new MutableLiveData<ProxyServerProvider>();
    public static synchronized ProxyController getInstance() {
        return INSTANCE;
    }

    public void startProxyService(Context context) {
        if(isRunningProxyService().getValue())return;
        startProxyServiceWithAction(context,ACTION_START_PROXY);
        isRunningProxyService.postValue(true);
    }

    public void stopProxyService(Context context) {
        startProxyServiceWithAction(context,ACTION_STOP_PROXY);
        isRunningProxyService.postValue(false);
    }
    
    public void toggleProxyServiceRunning(Context context) {
    	if(isRunningProxyService().getValue()){
            stopProxyService(context);
        }else{
            startProxyService(context);
        }
    }
    private void startProxyServiceWithAction(Context ctx,String action){
        Intent intent = new Intent(ctx,ProxyService.class);
        if(action!=null){
            intent.setAction(action);
        }
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
        	ContextCompat.startForegroundService(ctx,intent);
        } else {
            ctx.startService(intent);
        }
    }
    public LiveData<Boolean> isRunningProxyService() {
        return isRunningProxyService;
    }

    public interface ProxyServerProvider {
        IProxyServer provideServer();
    }

    public void setProxyServerProvider(ProxyServerProvider provider) {
    	if(provider!=null) {
    		proxyServerProviderLiveData.postValue(provider);
    	}
    }
    
    public void removeProxyServerProvider() {
    	proxyServerProviderLiveData.postValue(null);
    }

    public LiveData<ProxyServerProvider> getServerProvider() {
    	return proxyServerProviderLiveData;
    }
}

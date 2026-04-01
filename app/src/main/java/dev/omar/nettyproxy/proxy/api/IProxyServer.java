package dev.omar.nettyproxy.proxy.api;

public interface IProxyServer {

    public void startProxy();
    
    public void stopProxy();
    
    public boolean isRunningProxy();
    
    public String getIP();
    
    public int getPort();

}

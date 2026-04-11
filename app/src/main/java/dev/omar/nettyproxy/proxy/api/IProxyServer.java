package dev.omar.nettyproxy.proxy.api;

/**
 * Interface representing a Proxy Server.
 * Defines the essential operations for controlling the lifecycle of a proxy server
 * and accessing its configuration details.
 *
 * @author OMAR HAIDAR
 */
public interface IProxyServer {

    /**
     * Starts the proxy server.
     * The server must be in a stopped state prior to calling this method.
     */
    public void startProxy();

    /**
     * Stops the proxy server.
     * The server must be running prior to calling this method.
     */
    public void stopProxy();

    /**
     * Checks whether the proxy server is currently running.
     *
     * @return {@code true} if the server is running; {@code false} otherwise.
     */
    public boolean isRunningProxy();

    /**
     * Returns the IP address on which the proxy server is listening.
     *
     * @return the IP address as a String (e.g., "0.0.0.0" or "127.0.0.1").
     */
    public String getIP();

    /**
     * Returns the port number on which the proxy server is listening.
     *
     * @return the port number (an integer between 0 and 65535).
     */
    public int getPort();
}
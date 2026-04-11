package dev.omar.nettyproxy.proxy.netty;

import dev.omar.nettyproxy.proxy.api.IProxyServer;
import dev.omar.nettyproxy.utils.Utils;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import java.util.concurrent.atomic.AtomicLong;

public class HttpProxyServer implements IProxyServer {

    private final ChannelInitializer<SocketChannel> channelInitializer;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Thread serverThread;
    private volatile boolean isRunning = false;
    private final AtomicLong taskCounter = new AtomicLong(0);

    public HttpProxyServer(ChannelInitializer<SocketChannel> channelInitializer) {
        this.channelInitializer = channelInitializer;
    }

    public HttpProxyServer() {
        HttpProxyClientHandlerFactory factory =
                taskId -> {
                    HttpProxyClientHeader header = new HttpProxyClientHeader();
                    return new HttpProxyClientHandler("task-" + taskId, header);
                };

        ChannelInitializer<SocketChannel> initializer =
                new HttpProxyChannelInitializer(taskCounter, factory);
        this.channelInitializer = initializer;
    }

    private void shutdown() {
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public int getPort() {
        return 8080;
    }

    @Override
    public String getIP() {
        return Utils.getLocalIpAddress();
    }

    @Override
    public boolean isRunningProxy() {
        return isRunning;
    }

    @Override
    public void startProxy() {
        if (isRunning) {
            return;
        }

        serverThread =
                new Thread(
                        () -> {
                            isRunning = true;
                            bossGroup = new NioEventLoopGroup(1); // Boss group (يقبل الاتصالات)
                            workerGroup = new NioEventLoopGroup(); // Worker group (يعالج الطلبات)

                            try {

                                ServerBootstrap b = new ServerBootstrap();
                                b.group(bossGroup, workerGroup)
                                        .channel(NioServerSocketChannel.class)
                                        .handler(
                                                new LoggingHandler(
                                                        LogLevel.INFO)) 
                                        .childHandler(channelInitializer);

                                b.bind(getPort()).sync().channel().closeFuture().sync();

                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            } catch (Exception e) {
                            } finally {
                                shutdown();
                            }
                        },
                        "Proxy-Server-Thread");

        serverThread.start();
    }

    @Override
    public void stopProxy() {
        if (!isRunning) {
            return;
        }

        shutdown();

        if (serverThread != null) {
            serverThread.interrupt();
            serverThread = null;
        }

        isRunning = false;
    }
}

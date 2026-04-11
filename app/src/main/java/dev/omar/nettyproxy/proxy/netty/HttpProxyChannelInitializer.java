package dev.omar.nettyproxy.proxy.netty;

import java.util.concurrent.atomic.AtomicLong;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import android.util.Log;

public class HttpProxyChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static final String TAG = "ProxyChannelInitializer";

    private final AtomicLong taskCounter;
    private final HttpProxyClientHandlerFactory handlerFactory;

    /**
     * Constructor
     *
     * @param taskCounter عداد لتتبع عدد الطلبات (اختياري للـ logging)
     * @param handlerFactory مصنع لإنشاء HttpProxyClientHandler جديد لكل اتصال
     */
    public HttpProxyChannelInitializer(
            AtomicLong taskCounter, HttpProxyClientHandlerFactory handlerFactory) {
        this.taskCounter = taskCounter != null ? taskCounter : new AtomicLong(0);
        this.handlerFactory = handlerFactory;
    }

    public HttpProxyChannelInitializer() {
        this(null, null);
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        try {
            long taskId = taskCounter.getAndIncrement();

            ch.pipeline()
                    .addLast(
                            new LoggingHandler(
                                    LogLevel.INFO),
                            createClientHandler(taskId));

            Log.d(TAG, "تم تهيئة قناة جديدة - Task ID: " + taskId);

        } catch (Exception e) {
            Log.e(TAG, "خطأ أثناء تهيئة القناة", e);
            throw e;
        }
    }

    private HttpProxyClientHandler createClientHandler(long taskId) {
        String taskName = "task-" + taskId;
        HttpProxyClientHeader header = new HttpProxyClientHeader(); // header جديد لكل اتصال
        return new HttpProxyClientHandler(taskName, header);
    }
}

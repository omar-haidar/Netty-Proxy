package dev.omar.nettyproxy.proxy.netty;

import android.util.Log;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class HttpProxyClientHandler extends ChannelInboundHandlerAdapter {

    private static final String TAG = "HttpProxyClientHandler";

    private final String id;
    private Channel clientChannel;
    private Channel remoteChannel;

    // سنمرر الكائنات عبر الـ Constructor بدلاً من @Autowired
    private final HttpProxyClientHeader header;
    
    public HttpProxyClientHandler(String id, HttpProxyClientHeader header) {
        this.id = id;
        this.header = header;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        clientChannel = ctx.channel();
        Log.d(TAG, id + " - Client channel active");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (header.isComplete()) {
            if (remoteChannel != null && remoteChannel.isActive()) {
                remoteChannel.writeAndFlush(msg);
            } else {
                ((ByteBuf) msg).release();
            }
            return;
        }

        ByteBuf in = (ByteBuf) msg;
        header.digest(in);

        if (!header.isComplete()) {
            in.release();
            return;
        }

        Log.i(TAG, id + " " + header.toString());

        // تعطيل AutoRead مؤقتًا حتى يتم الاتصال بالخادم البعيد
        clientChannel.config().setAutoRead(false);

        if (header.isHttps()) {
            // إرسال 200 Connection Established للـ HTTPS Tunnel
            clientChannel.writeAndFlush(
                Unpooled.wrappedBuffer("HTTP/1.1 200 Connection Established\r\n\r\n".getBytes())
            );
        }

        // إنشاء اتصال بالخادم البعيد
        Bootstrap b = new Bootstrap();
        b.group(clientChannel.eventLoop())   // استخدام نفس EventLoop
                .channel(clientChannel.getClass())
                .handler(new HttpProxyRemoteHandler(id, clientChannel));   // سنحوله لاحقًا

        ChannelFuture f = b.connect(header.getHost(), header.getPort());
        remoteChannel = f.channel();

        f.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                clientChannel.config().setAutoRead(true);  // تفعيل القراءة مرة أخرى

                if (!header.isHttps()) {
                    // إرسال الـ Header + الباقي للطلبات العادية (HTTP)
                    remoteChannel.write(header.getByteBuf());
                }
                remoteChannel.writeAndFlush(in);
            } else {
                Log.e(TAG, id + " فشل الاتصال بالخادم البعيد: " + header.getHost() + ":" + header.getPort());
                in.release();
                clientChannel.close();
            }
        });
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        flushAndClose(remoteChannel);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Log.e(TAG, id + " خطأ في المعالجة", cause);
        flushAndClose(clientChannel);
    }

    private void flushAndClose(Channel ch) {
        if (ch != null && ch.isActive()) {
            ch.writeAndFlush(Unpooled.EMPTY_BUFFER)
                    .addListener(ChannelFutureListener.CLOSE);
        }
    }
}
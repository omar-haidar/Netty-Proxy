package dev.omar.nettyproxy.proxy.netty;

import android.util.Log;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * معالج البيانات القادمة من الخادم البعيد - نسخة أندرويد
 */
public class HttpProxyRemoteHandler extends ChannelInboundHandlerAdapter {

    private static final String TAG = "HttpProxyRemoteHandler";

    private final String id;
    private Channel clientChannel;
    private Channel remoteChannel;

    public HttpProxyRemoteHandler(String id, Channel clientChannel) {
        this.id = id;
        this.clientChannel = clientChannel;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.remoteChannel = ctx.channel();
        Log.d(TAG, id + " - Remote channel active");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (clientChannel != null && clientChannel.isActive()) {
            clientChannel.writeAndFlush(msg);
        } else {
            // تحرير الذاكرة إذا لم يعد هناك عميل
            if (msg instanceof ByteBuf) {
                ((ByteBuf) msg).release();
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        flushAndClose(clientChannel);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Log.e(TAG, id + " خطأ في Remote Handler", cause);
        flushAndClose(remoteChannel);
    }

    private void flushAndClose(Channel ch) {
        if (ch != null && ch.isActive()) {
            ch.writeAndFlush(Unpooled.EMPTY_BUFFER)
                    .addListener(ChannelFutureListener.CLOSE);
        }
    }
}
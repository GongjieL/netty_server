package com.gjie.netty.handler;

import com.gjie.netty.ConvertDelegater;
import com.gjie.netty.constant.HttpResponseMessage;
import com.gjie.netty.http.HttpData;
import com.gjie.netty.http.HttpDetailContent;
import com.gjie.netty.request.NettyHttpRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;

public class HttpNettyServerHandler extends SimpleChannelInboundHandler<NettyHttpRequest> {

    protected void messageReceived(ChannelHandlerContext channelHandlerContext, NettyHttpRequest nettyHttpRequest) throws Exception {
        HttpDetailContent httpDetailContent = nettyHttpRequest.getHttpDetailContent();
        Integer code = nettyHttpRequest.getCode();
        byte[] valueBytes = null;
        if (code != null) {
            valueBytes = code.toString().getBytes(Charset.forName("utf-8"));
        } else {
            Object content = nettyHttpRequest.getObject();
            HttpData responseData = httpDetailContent.getResponseData();
            Method method = httpDetailContent.getMethod();
            Object value = null;
            try {
                value = method.invoke(content);
            } catch (Exception e) {
                code = HttpResponseMessage.EXECPTION;
            }
            if (code == null) {
                if (responseData != null) {
                    String formatData = ConvertDelegater.convertFormatData(responseData.getFormatter(), value);
                    if (!formatData.isEmpty()) {
                        valueBytes = formatData.getBytes(Charset.forName("utf-8"));
                    }
                }
            } else {
                valueBytes = code.toString().getBytes(Charset.forName("utf-8"));
            }
        }
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        //处理数据
        ByteBuf byteBuf = Unpooled.copiedBuffer(valueBytes);
        response.content().writeBytes(byteBuf);
        byteBuf.release();
        response.headers().set(CONTENT_TYPE, "text/plain;charset=UTF-8");
        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }


}

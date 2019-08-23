package com.gjie.netty.handler;

import com.alibaba.fastjson.JSON;
import com.gjie.netty.ConvertDelegater;
import com.gjie.netty.constant.Formatter;
import com.gjie.netty.constant.HttpResponseMessageCode;
import com.gjie.netty.http.HttpData;
import com.gjie.netty.http.HttpDetailContent;
import com.gjie.netty.http.NettyHttpHandleData;
import com.gjie.netty.http.resp.HttpExceptionResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;

public class HttpNettyServerHandler extends SimpleChannelInboundHandler<NettyHttpHandleData> {

    protected void messageReceived(ChannelHandlerContext channelHandlerContext, NettyHttpHandleData nettyHttpHandleData) throws Exception {
        HttpDetailContent httpDetailContent = nettyHttpHandleData.getHttpDetailContent();
        HttpExceptionResponse httpResponseMessage = nettyHttpHandleData.getHttpResponseMessage();
        byte[] valueBytes = null;
        String contentType = Formatter.JSON.getContentType();
        if (httpResponseMessage != null) {
            valueBytes = JSON.toJSONString(httpResponseMessage).getBytes(Charset.forName("utf-8"));
        } else {
            Object content = nettyHttpHandleData.getObject();
            HttpData responseData = httpDetailContent.getResponseData();
            Method method = httpDetailContent.getMethod();
            Object value = null;
            try {
                value = method.invoke(method.getDeclaringClass().newInstance(), content);
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw, true));
                httpResponseMessage = new HttpExceptionResponse(
                        HttpResponseMessageCode.EXECPTION, String.format("%s execute error", httpDetailContent.getMethod().getName()),sw.toString());
            }
            if (httpResponseMessage == null) {
                if (responseData != null) {
                    contentType = responseData.getFormatter().getContentType();
                    String formatData = ConvertDelegater.convertFormatData(responseData.getFormatter(), value);
                    if (!formatData.isEmpty()) {
                        valueBytes = formatData.getBytes(Charset.forName("utf-8"));
                    }
                }
            } else {
                valueBytes = JSON.toJSONString(httpResponseMessage).getBytes(Charset.forName("utf-8"));
            }
        }
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        //处理数据
        ByteBuf byteBuf = Unpooled.copiedBuffer(valueBytes);
        response.content().writeBytes(byteBuf);
        byteBuf.release();
        response.headers().set(CONTENT_TYPE, contentType);
        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}

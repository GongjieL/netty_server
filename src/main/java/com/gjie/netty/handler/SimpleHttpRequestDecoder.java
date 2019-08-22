package com.gjie.netty.handler;

import com.gjie.netty.ConvertDelegater;
import com.gjie.netty.cache.impl.UrlMethodCache;
import com.gjie.netty.constant.HttpResponseMessage;
import com.gjie.netty.http.HttpData;
import com.gjie.netty.http.HttpDetailContent;
import com.gjie.netty.request.NettyHttpRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.FullHttpRequest;

import java.lang.reflect.Method;
import java.util.List;


public class SimpleHttpRequestDecoder extends MessageToMessageDecoder<FullHttpRequest> {
    public void decode(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest, List<Object> list) throws Exception {
        UrlMethodCache urlMethodCache = new UrlMethodCache();
        HttpDetailContent httpDetailContent = urlMethodCache.getData("url", fullHttpRequest.uri());

        if (check(httpDetailContent) != null) {
            list.add(new NettyHttpRequest(null, null, null, check(httpDetailContent)));
        } else {
            ByteBuf content = fullHttpRequest.content();
            int length = content.readableBytes();
            byte[] array = new byte[length];
            content.getBytes(content.readerIndex(), array);
            list.add(new NettyHttpRequest(fullHttpRequest, convertToObject(httpDetailContent, array), httpDetailContent, null));
        }
    }

    public Object convertToObject(HttpDetailContent httpDetailContent, byte[] array) {
        HttpData requestData = httpDetailContent.getRequestData();
        if (requestData == null) {
            return null;
        }
        return ConvertDelegater.convertToObject(requestData.getFormatter(), requestData.getType(), new String(array));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    private Integer check(HttpDetailContent httpDetailContent) {
        if (httpDetailContent == null) {
            return HttpResponseMessage.NO_EXISTS;
        }
        //参数不是0个，没有requestBody
        Method method = httpDetailContent.getMethod();
        HttpData requestData = httpDetailContent.getRequestData();
        if (method.getParameterCount() != 0 && requestData == null) {
            return HttpResponseMessage.EXECPTION;
        }
        HttpData responseData = httpDetailContent.getResponseData();
        if (!method.getReturnType().equals(Void.class) && requestData == null) {
            return HttpResponseMessage.EXECPTION;
        }
        return null;
    }
}

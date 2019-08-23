package com.gjie.netty.handler;

import com.gjie.netty.ConvertDelegater;
import com.gjie.netty.cache.impl.UrlMethodCache;
import com.gjie.netty.constant.HttpResponseMessageCode;
import com.gjie.netty.http.HttpData;
import com.gjie.netty.http.HttpDetailContent;
import com.gjie.netty.http.NettyHttpHandleData;
import com.gjie.netty.http.resp.HttpExceptionResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;

import java.lang.reflect.Method;
import java.util.List;


public class SimpleHttpRequestDecoder extends MessageToMessageDecoder<FullHttpRequest> {
    public void decode(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest, List<Object> list) throws Exception {
        UrlMethodCache urlMethodCache = new UrlMethodCache();
        HttpDetailContent httpDetailContent = urlMethodCache.getData("url", fullHttpRequest.uri());
        HttpExceptionResponse checkResponse = check(httpDetailContent, fullHttpRequest);
        if (checkResponse != null) {
            list.add(new NettyHttpHandleData(null, null, null, checkResponse));
        } else {
            ByteBuf content = fullHttpRequest.content();
            int length = content.readableBytes();
            byte[] array = new byte[length];
            content.getBytes(content.readerIndex(), array);
            list.add(new NettyHttpHandleData(fullHttpRequest, convertToObject(httpDetailContent, array), httpDetailContent, null));
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


    private HttpExceptionResponse check(HttpDetailContent httpDetailContent, FullHttpRequest fullHttpRequest) {
        HttpExceptionResponse httpExceptionResponse = null;
        if (httpDetailContent == null) {
            return new HttpExceptionResponse(
                    HttpResponseMessageCode.NO_EXISTS,
                    String.format("%s doesn't exist", fullHttpRequest.uri()),
                    null);
        }
        httpExceptionResponse = checkAnnotation(httpDetailContent);
        if (httpExceptionResponse != null) {
            return httpExceptionResponse;
        }
        return checkRequestMethod(httpDetailContent, fullHttpRequest);
    }

    private HttpExceptionResponse checkAnnotation(HttpDetailContent httpDetailContent) {
        //参数不是0个，没有requestBody
        Method method = httpDetailContent.getMethod();
        HttpData requestData = httpDetailContent.getRequestData();
        if (method.getParameterCount() != 0 && requestData == null) {
            return new HttpExceptionResponse(
                    HttpResponseMessageCode.NO_ANNOTION_REQUESTBODY,
                    String.format("%s has no requestBody annotation", httpDetailContent.getMethod().getName()),
                    null);
        }
        //返回值不是void但是没有responsebody
        HttpData responseData = httpDetailContent.getResponseData();
        if (!method.getReturnType().equals(Void.class) && responseData == null) {
            return new HttpExceptionResponse(
                    HttpResponseMessageCode.NO_ANNOTION_RESPONSEBODY,
                    String.format("%s has no responseBody annotation", httpDetailContent.getMethod().getName()),
                    null);
        }
        return null;
    }

    private HttpExceptionResponse checkRequestMethod(HttpDetailContent httpDetailContent, FullHttpRequest fullHttpRequest) {
        ByteBuf content = fullHttpRequest.content();
        HttpMethod httpMethod = fullHttpRequest.method();
        //校验parent method和自身的method是否相同
        if (httpDetailContent.getParentRequestMethod() != null
                && !httpDetailContent.getParentRequestMethod().equals(httpDetailContent.getNettyRequestMethod())) {
            return new HttpExceptionResponse(
                    HttpResponseMessageCode.PARENT_SELF_REQUEST_METHOD_NO_MATCH,
                    String.format("%s parent http request method doesn't equal self http request method", httpDetailContent.getUrl()),
                    null);
        }
        //校验当前请求方法和默认的方法
        if (!httpDetailContent.getNettyRequestMethod().toString().equals(httpMethod.toString())) {
            return new HttpExceptionResponse(
                    HttpResponseMessageCode.REQUEST_METHOD_ERROR,
                    String.format("%s method should be %s,but now is %s", httpDetailContent.getUrl(), httpDetailContent.getNettyRequestMethod().toString(), httpMethod.toString()),
                    null);
        }
        //get无content
        if (HttpMethod.GET.equals(httpMethod) && content.readableBytes() > 0) {
            return new HttpExceptionResponse(
                    HttpResponseMessageCode.BAD_REQUEST,
                    String.format("%s is GET request method,it should not have content", httpDetailContent.getUrl()),
                    null);
        }
        return null;
    }
}

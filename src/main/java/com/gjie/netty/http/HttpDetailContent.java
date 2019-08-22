package com.gjie.netty.http;

import com.gjie.netty.constant.NettyRequestMethod;

import java.lang.reflect.Method;

public class HttpDetailContent {
    private NettyRequestMethod parentRequestMethod;
    private String url;
    private NettyRequestMethod nettyRequestMethod;
    private Method method;
    private HttpData requestData;
    private HttpData responseData;

    public NettyRequestMethod getParentRequestMethod() {
        return parentRequestMethod;
    }

    public void setParentRequestMethod(NettyRequestMethod parentRequestMethod) {
        this.parentRequestMethod = parentRequestMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public NettyRequestMethod getNettyRequestMethod() {
        return nettyRequestMethod;
    }

    public void setNettyRequestMethod(NettyRequestMethod nettyRequestMethod) {
        this.nettyRequestMethod = nettyRequestMethod;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public HttpData getRequestData() {
        return requestData;
    }

    public void setRequestData(HttpData requestData) {
        this.requestData = requestData;
    }

    public HttpData getResponseData() {
        return responseData;
    }

    public void setResponseData(HttpData responseData) {
        this.responseData = responseData;
    }
}

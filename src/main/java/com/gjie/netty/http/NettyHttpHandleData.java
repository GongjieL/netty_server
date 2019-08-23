package com.gjie.netty.http;

import com.alibaba.fastjson.JSONObject;
import com.gjie.netty.http.HttpDetailContent;
import com.gjie.netty.http.resp.HttpExceptionResponse;
import io.netty.handler.codec.http.FullHttpRequest;

public class NettyHttpHandleData {
    private FullHttpRequest request;
    private Object object;
    private HttpDetailContent httpDetailContent;
    private HttpExceptionResponse httpResponseMessage;

    public FullHttpRequest getRequest() {
        return request;
    }

    public void setObject(JSONObject object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public void setRequest(FullHttpRequest request) {
        this.request = request;
    }


    public void setObject(Object object) {
        this.object = object;
    }

    public HttpDetailContent getHttpDetailContent() {
        return httpDetailContent;
    }

    public void setHttpDetailContent(HttpDetailContent httpDetailContent) {
        this.httpDetailContent = httpDetailContent;
    }

    public HttpExceptionResponse getHttpResponseMessage() {
        return httpResponseMessage;
    }

    public void setHttpResponseMessage(HttpExceptionResponse httpExceptionResponse) {
        this.httpResponseMessage = httpExceptionResponse;
    }


    public NettyHttpHandleData(FullHttpRequest request, Object object, HttpDetailContent httpDetailContent, HttpExceptionResponse httpExceptionResponse) {
        this.request = request;
        this.object = object;
        this.httpDetailContent = httpDetailContent;
        this.httpResponseMessage = httpExceptionResponse;
    }
}

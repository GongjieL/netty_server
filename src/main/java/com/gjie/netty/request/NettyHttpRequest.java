package com.gjie.netty.request;

import com.alibaba.fastjson.JSONObject;
import com.gjie.netty.http.HttpDetailContent;
import io.netty.handler.codec.http.FullHttpRequest;

public class NettyHttpRequest {
    private FullHttpRequest request;
    private Object object;
    private HttpDetailContent httpDetailContent;
    private Integer code;



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

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public NettyHttpRequest(FullHttpRequest request, Object object, HttpDetailContent httpDetailContent, Integer code) {
        this.request = request;
        this.object = object;
        this.httpDetailContent = httpDetailContent;
        this.code = code;
    }
}

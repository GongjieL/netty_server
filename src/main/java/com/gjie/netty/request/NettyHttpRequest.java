package com.gjie.netty.request;

import com.alibaba.fastjson.JSONObject;
import io.netty.handler.codec.http.FullHttpRequest;

public class NettyHttpRequest {
    private FullHttpRequest request;
    private JSONObject object;

    public NettyHttpRequest(FullHttpRequest request, JSONObject object) {
        this.request = request;
        this.object = object;
    }

    public FullHttpRequest getRequest() {
        return request;
    }

    public void setObject(JSONObject object) {
        this.object = object;
    }

    public JSONObject getObject() {
        return object;
    }

    public void setRequest(FullHttpRequest request) {
        this.request = request;
    }
}

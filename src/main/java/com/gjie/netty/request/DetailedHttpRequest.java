package com.gjie.netty.request;

import com.gjie.netty.constant.NettyRequestMethod;

import java.lang.reflect.Method;

/**
 * @author j_gong
 * @date 2019/8/22 6:57 PM
 */
public class DetailedHttpRequest {
    private NettyRequestMethod parentRequestMethod;
    private String url;
    private NettyRequestMethod nettyRequestMethod;
    private Method method;

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
}

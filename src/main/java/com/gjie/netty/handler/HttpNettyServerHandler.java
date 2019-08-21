package com.gjie.netty.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gjie.netty.request.NettyHttpRequest;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
public abstract class HttpNettyServerHandler extends SimpleChannelInboundHandler<NettyHttpRequest> {

    protected void messageReceived(ChannelHandlerContext channelHandlerContext, NettyHttpRequest nettyHttpRequest) throws Exception {
        //注册
        //参数
        JSONObject content = nettyHttpRequest.getObject();
        //根据url找方法

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        //处理数据
        ByteBuf byteBuf = Unpooled.copiedBuffer(JSON.toJSONString(service(content)).getBytes());
        response.content().writeBytes(byteBuf);
        byteBuf.release();
        response.headers().set(CONTENT_TYPE, "text/plain;charset=UTF-8");
        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }


}

package com.gjie.netty.handler.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gjie.netty.request.NettyHttpRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;

import java.util.List;


public class HttpJsonRequestDecoder extends MessageToMessageDecoder<FullHttpRequest> {
    protected void decode(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest, List<Object> list) throws Exception {
        ByteBuf content = fullHttpRequest.content();
        int length = content.readableBytes();
        byte[] array = new byte[length];
        content.getBytes(content.readerIndex(), array);
        JSONObject o = (JSONObject) JSON.parse(array);
        list.add(new NettyHttpRequest(fullHttpRequest,o));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}

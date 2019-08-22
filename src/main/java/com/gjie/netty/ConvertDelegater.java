package com.gjie.netty;

import com.alibaba.fastjson.JSON;
import com.gjie.netty.constant.Formatter;
import com.thoughtworks.xstream.XStream;

public class ConvertDelegater {

    public static Object convertToObject(Formatter formatter, Class type, String data) {
        if (Formatter.JSON.equals(formatter)) {
            return JSON.parseObject(data, type);
        } else if (Formatter.XML.equals(formatter)) {
            XStream xStream = new XStream();
            return type.cast(xStream.fromXML(data));
        }
        return null;
    }

    public static String convertFormatData(Formatter formatter, Object data) {
        if (data == null) {
            return null;
        }
        if (Formatter.JSON.equals(formatter)) {
            return JSON.toJSONString(data);
        } else if (Formatter.XML.equals(formatter)) {
            XStream xStream = new XStream();
            return xStream.toXML(data);
        }
        return data.toString();
    }
}

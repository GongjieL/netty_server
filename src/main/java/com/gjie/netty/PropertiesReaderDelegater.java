package com.gjie.netty;

import io.netty.util.internal.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author j_gong
 * @date 2019/8/22 11:18 AM
 */
public class PropertiesReaderDelegater {
    static Properties properties;

    static {
        InputStream resourceAsStream = PropertiesReaderDelegater.class.getClassLoader().getResourceAsStream("netty-server.properties");
        try {
            properties = new Properties();
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getProperty(String property) {
        String propertyValue = properties.getProperty(property);
        if (propertyValue == null || "".equals(propertyValue.trim())) {
            return null;
        }
        return Arrays.asList(propertyValue.split(","));
    }
}

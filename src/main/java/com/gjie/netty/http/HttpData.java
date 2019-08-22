package com.gjie.netty.http;

import com.gjie.netty.constant.Formatter;

public class HttpData {
    private Formatter formatter;
    private Class type;

    public HttpData(Formatter formatter, Class type) {
        this.formatter = formatter;
        this.type = type;
    }

    public Formatter getFormatter() {
        return formatter;
    }

    public void setFormatter(Formatter formatter) {
        this.formatter = formatter;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }
}

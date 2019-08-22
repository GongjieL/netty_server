package com.gjie.netty;

import com.gjie.netty.constant.InitEvent;

import java.util.Collections;
import java.util.List;

/**
 * @author j_gong
 * @date 2019/8/22 11:29 AM
 */
public class ServerInitEventDelegater {
    public void init() {
        List<String> scanPackages = PropertiesReaderDelegater.getProperty(InitEvent.SCAN_PACKAGES);
        if (scanPackages == null || scanPackages.size() == 0) {
            return;
        }
        //解析包名
        //刷入缓存

    }

}

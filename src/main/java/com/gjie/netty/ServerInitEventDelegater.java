package com.gjie.netty;

import com.gjie.netty.annotion.HttpWeb;
import com.gjie.netty.annotion.RequestMapping;
import com.gjie.netty.constant.InitEvent;
import com.gjie.netty.constant.NettyRequestMethod;
import com.gjie.netty.request.DetailedHttpRequest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
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
        List<Class> classes = AnalysisDelegater.listFitClasses(scanPackages);
        if (classes.isEmpty()) {
            return;
        }
        for (Class aClass : classes) {
            if (aClass.getAnnotation(HttpWeb.class) == null) {
                continue;
            }
            //获取类上的url
            RequestMapping parentRequestMapping = (RequestMapping) aClass.getAnnotation(RequestMapping.class);
            String parentUrl = null;
            NettyRequestMethod parentRequestMethod = null;
            if (parentRequestMapping != null) {
                parentUrl = parentRequestMapping.url();
                parentRequestMethod = parentRequestMapping.requestMethod();
            }
            //方法上的注解
            for (Method method : aClass.getMethods()) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                if (requestMapping == null) {
                    continue;
                }
                String url = requestMapping.url();
                NettyRequestMethod requestMethod = requestMapping.requestMethod();
                if (parentUrl != null) {
                    url = parentUrl + url;
                }
                DetailedHttpRequest detailedHttpRequest = new DetailedHttpRequest();
                detailedHttpRequest.setUrl(url);
                detailedHttpRequest.setNettyRequestMethod(requestMethod);
                detailedHttpRequest.setParentRequestMethod(parentRequestMethod);
                detailedHttpRequest.setMethod(method);
            }
        }
        //刷入缓存

    }
}

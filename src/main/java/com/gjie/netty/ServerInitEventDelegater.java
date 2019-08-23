package com.gjie.netty;

import com.gjie.netty.annotion.HttpWeb;
import com.gjie.netty.annotion.RequestBody;
import com.gjie.netty.annotion.RequestMapping;
import com.gjie.netty.annotion.ResponseBody;
import com.gjie.netty.cache.impl.UrlMethodCache;
import com.gjie.netty.constant.InitEvent;
import com.gjie.netty.constant.NettyRequestMethod;
import com.gjie.netty.http.HttpData;
import com.gjie.netty.http.HttpDetailContent;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author j_gong
 * @date 2019/8/22 11:29 AM
 */
public class ServerInitEventDelegater {

    public void init(String appName) {
        List<String> scanPackages = PropertiesReaderDelegater.getProperty(InitEvent.SCAN_PACKAGES);
        if (scanPackages == null || scanPackages.size() == 0) {
            return;
        }
        //解析包名
        List<Class> classes = AnalysisDelegater.listFitClasses(scanPackages);
        if (classes.isEmpty()) {
            return;
        }
        List<HttpDetailContent> httpDetailContents = new ArrayList<HttpDetailContent>();
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
                    url = new StringBuilder().append(appName).append(parentUrl).append(url).toString();
                } else {
                    url = new StringBuilder().append(appName).append(url).toString();
                }
                HttpDetailContent httpDetailContent = new HttpDetailContent();
                httpDetailContent.setUrl(url);
                httpDetailContent.setNettyRequestMethod(requestMethod);
                httpDetailContent.setParentRequestMethod(parentRequestMethod);
                httpDetailContent.setMethod(method);
                //获取参数注解
                RequestBody requestBody = null;
                Class parameterClass = null;
                Parameter[] parameters = method.getParameters();
                if (parameters != null) {
                    for (Parameter parameter : parameters) {
                        RequestBody parameterAnnotation = parameter.getAnnotation(RequestBody.class);
                        requestBody = parameterAnnotation;
                        parameterClass = parameter.getType();
                        break;
                    }
                }
                if (requestBody != null) {
                    httpDetailContent.setRequestData(new HttpData(requestBody.format(), parameterClass));
                }
                //获取返回参数
                ResponseBody responseBody = method.getAnnotation(ResponseBody.class);
                if (requestBody != null) {
                    httpDetailContent.setResponseData(new HttpData(responseBody.format(), method.getReturnType()));
                }
                httpDetailContents.add(httpDetailContent);
            }
        }
        //刷入缓存
        UrlMethodCache urlMethodCache = new UrlMethodCache();
        urlMethodCache.importData(httpDetailContents);
    }
}

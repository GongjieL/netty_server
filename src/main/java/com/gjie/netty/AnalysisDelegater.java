package com.gjie.netty;

import com.gjie.netty.annotion.HttpWeb;
import com.gjie.netty.constant.InitEvent;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author j_gong
 * @date 2019/8/22 11:29 AM
 */
public class AnalysisDelegater {

    public String init(String resourceValue) {
        String[] basePackages = resourceValue.split(InitEvent.PACKAGE_SPLIT_SEPARATOR);
        if (basePackages == null || basePackages.length == 0) {
            return null;
        }
        List<Class> classes = listFitClasses(basePackages);
        for (Class aClass : classes) {
            Annotation annotation = aClass.getAnnotation(HttpWeb.class);
            if(annotation==null){
                continue;
            }

        }
        return null;
    }

    private List<Class> listFitClasses(String[] basePackages) {
        if (basePackages == null || basePackages.length == 0) {
            return null;
        }
        final URL rootUrl = Thread.currentThread().getContextClassLoader().getResource("");
        File rootFile = new File(rootUrl.getPath());
        List<String> fitClassNames = new ArrayList<String>();
        for (String packageRule : basePackages) {
            fitClassNames.addAll(
                    listPackageClasses(rootFile, packageRule, rootFile.getPath())
            );
        }
        //去除重复
        fitClassNames = fitClassNames.stream().distinct().collect(Collectors.toList());
        List<Class> fitClasses = new ArrayList<>();
        fitClassNames.forEach(f -> {
            f = f.substring(rootUrl.getPath().length());
            try {
                String className = f.replaceAll("/", ".");
                className = className.substring(0, className.indexOf(".class"));
                fitClasses.add(Class.forName(className));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        return fitClasses;
    }

    public static void main(String[] args) throws MalformedURLException {
        List<Class> classes = new AnalysisDelegater().listFitClasses(new String[]{"com.*.*.*"});
//        URL rootUrl = Thread.currentThread().getContextClassLoader().getResource("");
//        File rootFile = new File(rootUrl.getPath());
//        List<String> list = listPackageClasses(rootFile, "com.*.netty.*.json", rootFile.getPath());
        System.out.println(classes);
    }

    public List<String> listPackageClasses(File file, String matchSymbol, String rootPath) {
        List<String> list = new ArrayList<String>();
        String compareSymbol = matchSymbol;
        int index = matchSymbol.indexOf(".");
        boolean isRoot = rootPath.equals(file.getPath());
        if (!isRoot && index != -1) {
            compareSymbol = matchSymbol.substring(0, index);
        }
        boolean canContinue = isRoot || file.getName().equals(compareSymbol) || InitEvent.COMMON_SYSMBOL.equals(compareSymbol);
        if (file.isDirectory()) {
            if (canContinue) {
                File[] files = file.listFiles();
                for (File subFile : files) {
                    String temp = matchSymbol;
                    if (!isRoot) {
                        temp = matchSymbol.substring(index + 1);
                    }
                    if (index == -1) {
                        temp = "";
                    }
                    list.addAll(listPackageClasses(subFile, temp, rootPath));
                }
            }
        } else {
            if ("".equals(matchSymbol) && file.getPath().endsWith(".class")) {
                list.add(file.getPath());
            }
        }
        return list;
    }
}

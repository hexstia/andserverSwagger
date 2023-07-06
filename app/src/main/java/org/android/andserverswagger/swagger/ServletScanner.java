package org.android.andserverswagger.swagger;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import android.util.ArraySet;
import android.util.Log;

import com.yanzhenjie.andserver.annotation.Controller;

import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.config.Scanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;

import org.android.andserverswagger.App;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public class ServletScanner implements Scanner {
    private boolean prettyPrint = false;
    private String resourcePackage;

    public ServletScanner(HashMap<String,String> servletConfig) {
//        this.resourcePackage = servletConfig.getInitParameter("swagger.resource.package");

//        String shouldPrettyPrint = servletConfig.getInitParameter("swagger.pretty.print");
        this.resourcePackage = servletConfig.get("swagger.resource.package");
        String shouldPrettyPrint = servletConfig.get("swagger.pretty.print");
        if (shouldPrettyPrint != null) {
            this.prettyPrint = Boolean.parseBoolean(shouldPrettyPrint);
        }

    }

    public String getResourcePackage() {
        return this.resourcePackage;
    }

    public void setResourcePackage(String resourcePackage) {
        this.resourcePackage = resourcePackage;
    }

    public Set<Class<?>> classes() {

//        return (new Reflections(this.resourcePackage, new org.reflections.scanners.Scanner[0])).getTypesAnnotatedWith(Api.class);
        return scanPackageByAnnotation(resourcePackage,Api.class);
    }

    public boolean getPrettyPrint() {
        return this.prettyPrint;
    }

    public void setPrettyPrint(boolean shouldPrettyPrint) {
        this.prettyPrint = shouldPrettyPrint;
    }

    public static Set<Class<?>> scanPackageByAnnotation(String packageName, final Class<? extends Annotation> annotationClass) {
//		List<Class<?>> classes = new ArrayList<Class<?>>();
        Set<Class<?>> set = new ArraySet<>();
        try {
            PathClassLoader classLoader = (PathClassLoader) Thread
                    .currentThread().getContextClassLoader();

            DexFile dex = new DexFile(App.getContext().getPackageResourcePath());
            Enumeration<String> entries = dex.entries();
            while (entries.hasMoreElements()) {
                String entryName = entries.nextElement();
                if (entryName.contains(packageName)) {
                    Class<?> entryClass = Class.forName(entryName, true,classLoader);//疑问：Class.forName(entryName);这种方式不知道为什么返回null，哪位大神知道原因，请指点一下小弟吧  感激不尽
                    Annotation annotation = entryClass.getAnnotation(annotationClass);
                    if (annotation != null) {
                        set.add(entryClass);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return set;
    }
}

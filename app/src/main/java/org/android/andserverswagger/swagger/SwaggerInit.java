package org.android.andserverswagger.swagger;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.HashMap;
import java.util.Set;

import io.swagger.config.Scanner;
import io.swagger.config.SwaggerConfig;
import io.swagger.models.Swagger;
import io.swagger.util.Json;

public class SwaggerInit {
    /**
     *  需要注解的扫描的包
     */
    private String scanResourcePackage;
    /**
     * 漂亮的打印 true false
     */
    private String  isPrettyprint;
    /**
     *  基础的baseurl路径 http://localhost:8002
     */
    private String  bastPath;
    /**
     * api 的版本
     */
    private String  apiVersion;

    private Swagger swagger;
    public SwaggerInit(String scanResourcePackage, String isPrettyprint, String bastPath, String apiVersion) {
        this.scanResourcePackage = scanResourcePackage;
        this.isPrettyprint = isPrettyprint;
        this.bastPath = bastPath;
        this.apiVersion = apiVersion;
    }

    public Swagger getSwagger() {
        return swagger;
    }

    public void setSwagger(Swagger swagger) {
        this.swagger = swagger;
    }

    public void init(){
        HashMap<String,String> swaggerInfoConfig = new HashMap<>();
        swaggerInfoConfig.put("swagger.resource.package",scanResourcePackage);
        swaggerInfoConfig.put("swagger.pretty.print",isPrettyprint);
        swaggerInfoConfig.put("swagger.api.basepath",bastPath);
        swaggerInfoConfig.put("api.version",apiVersion);
        ServletScanner servletScanner = new ServletScanner(swaggerInfoConfig);
        WebXMLReader webXMLReader = new WebXMLReader(swaggerInfoConfig);
        Scanner scanner =servletScanner;
        if (scanner != null) {
            if (swagger == null) {
                swagger = new Swagger();
            }

            SwaggerConfig configurator = (SwaggerConfig)webXMLReader;
            if (configurator != null) {
                configurator.configure(swagger);
            }

            Set<Class<?>> classes = scanner.classes();
            if (classes != null) {
                Log.i("TAG","======"+classes.size());
                Reader.read(swagger, classes);
            }
            try {
               Log.i("TAG",Json.mapper().writeValueAsString(swagger));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            setSwagger(swagger);

        }
    }

}

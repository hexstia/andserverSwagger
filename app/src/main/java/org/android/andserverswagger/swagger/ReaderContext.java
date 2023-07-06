package org.android.andserverswagger.swagger;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import io.swagger.models.Swagger;
import io.swagger.models.parameters.Parameter;
import java.util.List;

public class ReaderContext {
    private Swagger swagger;
    private Class<?> cls;
    private String parentPath;
    private String parentHttpMethod;
    private boolean readHidden;
    private List<String> parentConsumes;
    private List<String> parentProduces;
    private List<String> parentTags;
    private List<Parameter> parentParameters;

    public ReaderContext(Swagger swagger, Class<?> cls, String parentPath, String parentHttpMethod, boolean readHidden, List<String> parentConsumes, List<String> parentProduces, List<String> parentTags, List<Parameter> parentParameters) {
        this.setSwagger(swagger);
        this.setCls(cls);
        this.setParentPath(parentPath);
        this.setParentHttpMethod(parentHttpMethod);
        this.setReadHidden(readHidden);
        this.setParentConsumes(parentConsumes);
        this.setParentProduces(parentProduces);
        this.setParentTags(parentTags);
        this.setParentParameters(parentParameters);
    }

    public Swagger getSwagger() {
        return this.swagger;
    }

    public void setSwagger(Swagger swagger) {
        this.swagger = swagger;
    }

    public Class<?> getCls() {
        return this.cls;
    }

    public void setCls(Class<?> cls) {
        this.cls = cls;
    }

    public String getParentPath() {
        return this.parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public String getParentHttpMethod() {
        return this.parentHttpMethod;
    }

    public void setParentHttpMethod(String parentHttpMethod) {
        this.parentHttpMethod = parentHttpMethod;
    }

    public boolean isReadHidden() {
        return this.readHidden;
    }

    public void setReadHidden(boolean readHidden) {
        this.readHidden = readHidden;
    }

    public List<String> getParentConsumes() {
        return this.parentConsumes;
    }

    public void setParentConsumes(List<String> parentConsumes) {
        this.parentConsumes = parentConsumes;
    }

    public List<String> getParentProduces() {
        return this.parentProduces;
    }

    public void setParentProduces(List<String> parentProduces) {
        this.parentProduces = parentProduces;
    }

    public List<String> getParentTags() {
        return this.parentTags;
    }

    public void setParentTags(List<String> parentTags) {
        this.parentTags = parentTags;
    }

    public List<Parameter> getParentParameters() {
        return this.parentParameters;
    }

    public void setParentParameters(List<Parameter> parentParameters) {
        this.parentParameters = parentParameters;
    }
}

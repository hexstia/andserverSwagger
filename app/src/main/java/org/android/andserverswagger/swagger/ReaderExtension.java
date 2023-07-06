package org.android.andserverswagger.swagger;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import io.swagger.models.Operation;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public interface ReaderExtension {
    int getPriority();

    boolean isReadable(ReaderContext var1);

    void applyConsumes(ReaderContext var1, Operation var2, Method var3);

    void applyProduces(ReaderContext var1, Operation var2, Method var3);

    String getHttpMethod(ReaderContext var1, Method var2);

    String getPath(ReaderContext var1, Method var2);

    void applyOperationId(Operation var1, Method var2);

    void applySummary(Operation var1, Method var2);

    void applyDescription(Operation var1, Method var2);

    void applySchemes(ReaderContext var1, Operation var2, Method var3);

    void setDeprecated(Operation var1, Method var2);

    void applySecurityRequirements(ReaderContext var1, Operation var2, Method var3);

    void applyTags(ReaderContext var1, Operation var2, Method var3);

    void applyResponses(ReaderContext var1, Operation var2, Method var3);

    void applyParameters(ReaderContext var1, Operation var2, Type var3, Annotation[] var4);

    void applyImplicitParameters(ReaderContext var1, Operation var2, Method var3);

    void applyExtensions(ReaderContext var1, Operation var2, Method var3);
}

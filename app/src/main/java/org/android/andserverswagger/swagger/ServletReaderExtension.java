package org.android.andserverswagger.swagger;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Collections2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import io.swagger.annotations.ResponseHeader;
import io.swagger.converter.ModelConverters;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Response;
import io.swagger.models.Scheme;
import io.swagger.models.SecurityRequirement;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.FormParameter;
import io.swagger.models.parameters.HeaderParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.util.BaseReaderUtils;
import io.swagger.util.ParameterProcessor;
import io.swagger.util.PathUtils;
import io.swagger.util.ReflectionUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServletReaderExtension implements ReaderExtension {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServletReaderExtension.class);
    private static final String SUCCESSFUL_OPERATION = "successful operation";

    public ServletReaderExtension() {
    }

    private static <T> List<T> parseAnnotationValues(String str, Function<String, T> processor) {
        List<T> result = new ArrayList();
        Iterator var3 = Splitter.on(",").trimResults().omitEmptyStrings().split(str).iterator();

        while(var3.hasNext()) {
            String item = (String)var3.next();
            result.add(processor.apply(item));
        }

        return result;
    }

    private static List<String> parseStringValues(String str) {
        return parseAnnotationValues(str, new Function<String, String>() {
            public String apply(String value) {
                return value;
            }
        });
    }

    private static List<Scheme> parseSchemes(String schemes) {
        List<Scheme> result = new ArrayList();
        String[] var2 = StringUtils.trimToEmpty(schemes).split(",");
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String item = var2[var4];
            Scheme scheme = Scheme.forValue(StringUtils.trimToNull(item));
            if (scheme != null && !result.contains(scheme)) {
                result.add(scheme);
            }
        }

        return result;
    }

    private static List<SecurityRequirement> parseAuthorizations(Authorization[] authorizations) {
        List<SecurityRequirement> result = new ArrayList();
        Authorization[] var2 = authorizations;
        int var3 = authorizations.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Authorization auth = var2[var4];
            if (StringUtils.isNotEmpty(auth.value())) {
                SecurityRequirement security = new SecurityRequirement();
                security.setName(auth.value());
                AuthorizationScope[] var7 = auth.scopes();
                int var8 = var7.length;

                for(int var9 = 0; var9 < var8; ++var9) {
                    AuthorizationScope scope = var7[var9];
                    if (StringUtils.isNotEmpty(scope.scope())) {
                        security.addScope(scope.scope());
                    }
                }

                result.add(security);
            }
        }

        return result;
    }

    private static Map<String, Property> parseResponseHeaders(ReaderContext context, ResponseHeader[] headers) {
        Map<String, Property> responseHeaders = null;
        ResponseHeader[] var3 = headers;
        int var4 = headers.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            ResponseHeader header = var3[var5];
            String name = header.name();
            if (StringUtils.isNotEmpty(name)) {
                if (responseHeaders == null) {
                    responseHeaders = new HashMap();
                }

                Class<?> cls = header.response();
                if (!ReflectionUtils.isVoid(cls)) {
                    Property property = ModelConverters.getInstance().readAsProperty(cls);
                    if (property != null) {
                        Property responseProperty = ContainerWrapper.wrapContainer(header.responseContainer(), property, ContainerWrapper.ARRAY, ContainerWrapper.LIST, ContainerWrapper.SET);
                        responseProperty.setDescription(header.description());
                        responseHeaders.put(name, responseProperty);
                        appendModels(context.getSwagger(), cls);
                    }
                }
            }
        }

        return responseHeaders;
    }

    private static void appendModels(Swagger swagger, Type type) {
        Map<String, Model> models = ModelConverters.getInstance().readAll(type);
        Iterator var3 = models.entrySet().iterator();

        while(var3.hasNext()) {
            Map.Entry<String, Model> entry = (Map.Entry)var3.next();
            swagger.model((String)entry.getKey(), (Model)entry.getValue());
        }

    }

    private static boolean isValidResponse(Type type) {
        JavaType javaType = TypeFactory.defaultInstance().constructType(type);
        return !ReflectionUtils.isVoid(javaType);
    }

    private static Type getResponseType(Method method) {
        ApiOperation apiOperation = (ApiOperation)ReflectionUtils.getAnnotation(method, ApiOperation.class);
        return (Type)(apiOperation != null && !ReflectionUtils.isVoid(apiOperation.response()) ? apiOperation.response() : method.getGenericReturnType());
    }

    private static String getResponseContainer(ApiOperation apiOperation) {
        return apiOperation == null ? null : (String)StringUtils.defaultIfBlank(apiOperation.responseContainer(), (CharSequence)null);
    }

    public int getPriority() {
        return 0;
    }

    public boolean isReadable(ReaderContext context) {
        Api apiAnnotation = (Api)context.getCls().getAnnotation(Api.class);
        return apiAnnotation != null && (context.isReadHidden() || !apiAnnotation.hidden());
    }

    public void applyConsumes(ReaderContext context, Operation operation, Method method) {
        List<String> consumes = new ArrayList();
        ApiOperation apiOperation = (ApiOperation)ReflectionUtils.getAnnotation(method, ApiOperation.class);
        if (apiOperation != null) {
            consumes.addAll(parseStringValues(apiOperation.consumes()));
        }

        if (consumes.isEmpty()) {
            Api apiAnnotation = (Api)context.getCls().getAnnotation(Api.class);
            if (apiAnnotation != null) {
                consumes.addAll(parseStringValues(apiAnnotation.consumes()));
            }

            consumes.addAll(context.getParentConsumes());
        }

        Iterator var8 = consumes.iterator();

        while(var8.hasNext()) {
            String consume = (String)var8.next();
            operation.consumes(consume);
        }

    }

    public void applyProduces(ReaderContext context, Operation operation, Method method) {
        List<String> produces = new ArrayList();
        ApiOperation apiOperation = (ApiOperation)ReflectionUtils.getAnnotation(method, ApiOperation.class);
        if (apiOperation != null) {
            produces.addAll(parseStringValues(apiOperation.produces()));
        }

        if (produces.isEmpty()) {
            Api apiAnnotation = (Api)context.getCls().getAnnotation(Api.class);
            if (apiAnnotation != null) {
                produces.addAll(parseStringValues(apiAnnotation.produces()));
            }

            produces.addAll(context.getParentProduces());
        }

        Iterator var8 = produces.iterator();

        while(var8.hasNext()) {
            String produce = (String)var8.next();
            operation.produces(produce);
        }

    }

    public String getHttpMethod(ReaderContext context, Method method) {
        ApiOperation apiOperation = (ApiOperation)ReflectionUtils.getAnnotation(method, ApiOperation.class);
        return apiOperation != null && !StringUtils.isEmpty(apiOperation.httpMethod()) ? apiOperation.httpMethod() : context.getParentHttpMethod();
    }

    public String getPath(ReaderContext context, Method method) {
        Api apiAnnotation = (Api)context.getCls().getAnnotation(Api.class);
        ApiOperation apiOperation = (ApiOperation)ReflectionUtils.getAnnotation(method, ApiOperation.class);
        String operationPath = apiOperation == null ? null : apiOperation.nickname();
        return PathUtils.collectPath(new String[]{context.getParentPath(), apiAnnotation == null ? null : apiAnnotation.value(), StringUtils.isBlank(operationPath) ? method.getName() : operationPath});
    }

    public void applyOperationId(Operation operation, Method method) {
        ApiOperation apiOperation = (ApiOperation)ReflectionUtils.getAnnotation(method, ApiOperation.class);
        if (apiOperation != null && StringUtils.isNotBlank(apiOperation.nickname())) {
            operation.operationId(apiOperation.nickname());
        } else {
            operation.operationId(method.getName());
        }

    }

    public void applySummary(Operation operation, Method method) {
        ApiOperation apiOperation = (ApiOperation)ReflectionUtils.getAnnotation(method, ApiOperation.class);
        if (apiOperation != null && StringUtils.isNotBlank(apiOperation.value())) {
            operation.summary(apiOperation.value());
        }

    }

    public void applyDescription(Operation operation, Method method) {
        ApiOperation apiOperation = (ApiOperation)ReflectionUtils.getAnnotation(method, ApiOperation.class);
        if (apiOperation != null && StringUtils.isNotBlank(apiOperation.notes())) {
            operation.description(apiOperation.notes());
        }

    }

    public void applySchemes(ReaderContext context, Operation operation, Method method) {
        List<Scheme> schemes = new ArrayList();
        ApiOperation apiOperation = (ApiOperation)ReflectionUtils.getAnnotation(method, ApiOperation.class);
        Api apiAnnotation = (Api)context.getCls().getAnnotation(Api.class);
        if (apiOperation != null) {
            schemes.addAll(parseSchemes(apiOperation.protocols()));
        }

        if (schemes.isEmpty() && apiAnnotation != null) {
            schemes.addAll(parseSchemes(apiAnnotation.protocols()));
        }

        Iterator var7 = schemes.iterator();

        while(var7.hasNext()) {
            Scheme scheme = (Scheme)var7.next();
            operation.scheme(scheme);
        }

    }

    public void setDeprecated(Operation operation, Method method) {
        if (ReflectionUtils.getAnnotation(method, Deprecated.class) != null) {
            operation.deprecated(true);
        }

    }

    public void applySecurityRequirements(ReaderContext context, Operation operation, Method method) {
        List<SecurityRequirement> securityRequirements = new ArrayList();
        ApiOperation apiOperation = (ApiOperation)ReflectionUtils.getAnnotation(method, ApiOperation.class);
        Api apiAnnotation = (Api)context.getCls().getAnnotation(Api.class);
        if (apiOperation != null) {
            securityRequirements.addAll(parseAuthorizations(apiOperation.authorizations()));
        }

        if (securityRequirements.isEmpty() && apiAnnotation != null) {
            securityRequirements.addAll(parseAuthorizations(apiAnnotation.authorizations()));
        }

        Iterator var7 = securityRequirements.iterator();

        while(var7.hasNext()) {
            SecurityRequirement securityRequirement = (SecurityRequirement)var7.next();
            operation.security(securityRequirement);
        }

    }

    public void applyTags(ReaderContext context, Operation operation, Method method) {
        List<String> tags = new ArrayList();
        Api apiAnnotation = (Api)context.getCls().getAnnotation(Api.class);
        Predicate predicate = new Predicate<String>() {
            public boolean apply(String input) {
                return StringUtils.isNotBlank(input);
            }

            public boolean test(String input) {
                return this.apply(input);
            }
        };
        if (apiAnnotation != null) {
            tags.addAll(Collections2.filter(Arrays.asList(apiAnnotation.tags()), predicate));
        }

        tags.addAll(context.getParentTags());
        ApiOperation apiOperation = (ApiOperation)ReflectionUtils.getAnnotation(method, ApiOperation.class);
        if (apiOperation != null) {
            tags.addAll(Collections2.filter(Arrays.asList(apiOperation.tags()), predicate));
        }

        Iterator var8 = tags.iterator();

        while(var8.hasNext()) {
            String tag = (String)var8.next();
            operation.tag(tag);
        }

    }

    public void applyResponses(ReaderContext context, Operation operation, Method method) {
        Map<Integer, Response> result = new HashMap();
        ApiOperation apiOperation = (ApiOperation)ReflectionUtils.getAnnotation(method, ApiOperation.class);
        if (apiOperation != null && StringUtils.isNotBlank(apiOperation.responseReference())) {
            Response response = (new Response()).description("successful operation");
            response.schema(new RefProperty(apiOperation.responseReference()));
            result.put(apiOperation.code(), response);
        }

        Type responseType = getResponseType(method);
        int responseCode;
        if (isValidResponse(responseType)) {
            Property property = ModelConverters.getInstance().readAsProperty(responseType);
            if (property != null) {
                Property responseProperty = ContainerWrapper.wrapContainer(getResponseContainer(apiOperation), property);
                responseCode = apiOperation == null ? 200 : apiOperation.code();
                Map<String, Property> defaultResponseHeaders = apiOperation == null ? Collections.emptyMap() : parseResponseHeaders(context, apiOperation.responseHeaders());
                Response response = (new Response()).description("successful operation").schema(responseProperty).headers(defaultResponseHeaders);
                result.put(responseCode, response);
                appendModels(context.getSwagger(), responseType);
            }
        }

        ApiResponses responseAnnotation = (ApiResponses)ReflectionUtils.getAnnotation(method, ApiResponses.class);
        if (responseAnnotation != null) {
            ApiResponse[] var18 = responseAnnotation.value();
            responseCode = var18.length;

            for(int var21 = 0; var21 < responseCode; ++var21) {
                ApiResponse apiResponse = var18[var21];
                Map<String, Property> responseHeaders = parseResponseHeaders(context, apiResponse.responseHeaders());
                Response response = (new Response()).description(apiResponse.message()).headers(responseHeaders);
                if (StringUtils.isNotEmpty(apiResponse.reference())) {
                    response.schema(new RefProperty(apiResponse.reference()));
                } else if (!ReflectionUtils.isVoid(apiResponse.response())) {
                    Type type = apiResponse.response();
                    Property property = ModelConverters.getInstance().readAsProperty(type);
                    if (property != null) {
                        response.schema(ContainerWrapper.wrapContainer(apiResponse.responseContainer(), property));
                        appendModels(context.getSwagger(), type);
                    }
                }

                result.put(apiResponse.code(), response);
            }
        }

        Iterator var19 = result.entrySet().iterator();

        while(var19.hasNext()) {
            Map.Entry<Integer, Response> responseEntry = (Map.Entry)var19.next();
            if ((Integer)responseEntry.getKey() == 0) {
                operation.defaultResponse((Response)responseEntry.getValue());
            } else {
                operation.response((Integer)responseEntry.getKey(), (Response)responseEntry.getValue());
            }
        }

    }

    public void applyParameters(ReaderContext context, Operation operation, Type type, Annotation[] annotations) {
    }

    public void applyImplicitParameters(ReaderContext context, Operation operation, Method method) {
        ApiImplicitParams implicitParams = (ApiImplicitParams)ReflectionUtils.getAnnotation(method, ApiImplicitParams.class);
        if (implicitParams != null && implicitParams.value().length > 0) {
            ApiImplicitParam[] var5 = implicitParams.value();
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                ApiImplicitParam param = var5[var7];
                Parameter p = this.readImplicitParam(context.getSwagger(), param);
                if (p != null) {
                    operation.parameter(p);
                }
            }
        }

    }

    public void applyExtensions(ReaderContext context, Operation operation, Method method) {
        ApiOperation apiOperation = (ApiOperation)ReflectionUtils.getAnnotation(method, ApiOperation.class);
        if (apiOperation != null) {
            operation.getVendorExtensions().putAll(BaseReaderUtils.parseExtensions(apiOperation.extensions()));
        }

    }

    private Parameter readImplicitParam(Swagger swagger, ApiImplicitParam param) {
        Parameter p = ParameterFactory.createParam(param.paramType());
        if (p == null) {
            return null;
        } else {
            Type type = ReflectionUtils.typeFromString(param.dataType());
            return ParameterProcessor.applyAnnotations(swagger, p, (Type)(type == null ? String.class : type), Collections.singletonList(param));
        }
    }

    static enum ContainerWrapper {
        LIST("list") {
            protected Property doWrap(Property property) {
                return new ArrayProperty(property);
            }
        },
        ARRAY("array") {
            protected Property doWrap(Property property) {
                return new ArrayProperty(property);
            }
        },
        MAP("map") {
            protected Property doWrap(Property property) {
                return new MapProperty(property);
            }
        },
        SET("set") {
            protected Property doWrap(Property property) {
                ArrayProperty arrayProperty = new ArrayProperty(property);
                arrayProperty.setUniqueItems(true);
                return arrayProperty;
            }
        };

        private final String container;

        private ContainerWrapper(String container) {
            this.container = container;
        }

        public static Property wrapContainer(String container, Property property, ContainerWrapper... allowed) {
            Set<ContainerWrapper> tmp = allowed.length > 0 ? EnumSet.copyOf(Arrays.asList(allowed)) : EnumSet.allOf(ContainerWrapper.class);
            Iterator var4 = tmp.iterator();

            Property prop;
            do {
                if (!var4.hasNext()) {
                    return property;
                }

                ContainerWrapper wrapper = (ContainerWrapper)var4.next();
                prop = wrapper.wrap(container, property);
            } while(prop == null);

            return prop;
        }

        public Property wrap(String container, Property property) {
            return this.container.equalsIgnoreCase(container) ? this.doWrap(property) : null;
        }

        protected abstract Property doWrap(Property var1);
    }

    static enum ParameterFactory {
        PATH("path") {
            protected Parameter create() {
                return new PathParameter();
            }
        },
        QUERY("query") {
            protected Parameter create() {
                return new QueryParameter();
            }
        },
        FORM("form") {
            protected Parameter create() {
                return new FormParameter();
            }
        },
        FORM_DATA("formData") {
            protected Parameter create() {
                return new FormParameter();
            }
        },
        HEADER("header") {
            protected Parameter create() {
                return new HeaderParameter();
            }
        },
        BODY("body") {
            protected Parameter create() {
                return new BodyParameter();
            }
        };

        private final String paramType;

        private ParameterFactory(String paramType) {
            this.paramType = paramType;
        }

        public static Parameter createParam(String paramType) {
            ParameterFactory[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                ParameterFactory item = var1[var3];
                if (item.paramType.equalsIgnoreCase(paramType)) {
                    return item.create();
                }
            }

            ServletReaderExtension.LOGGER.warn("Unknown implicit parameter type: [" + paramType + "]");
            return null;
        }

        protected abstract Parameter create();
    }
}

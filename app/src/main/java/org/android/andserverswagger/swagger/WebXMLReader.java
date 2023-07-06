package org.android.andserverswagger.swagger;

import io.swagger.config.FilterFactory;
import io.swagger.config.SwaggerConfig;
import io.swagger.core.filter.SwaggerSpecFilter;
import io.swagger.models.Info;
import io.swagger.models.Scheme;
import io.swagger.models.Swagger;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class WebXMLReader implements SwaggerConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebXMLReader.class);
    private final String filterClass;
    private final String apiVersion;
    private final String title;
    private String basePath;
    private String host;
    private Scheme scheme;

    public WebXMLReader(HashMap<String,String> servletConfig) {
        this.scheme = Scheme.HTTP;
        this.apiVersion = getInitParameter(servletConfig, "api.version", "Swagger Server");
        this.basePath = getInitParameter(servletConfig, "swagger.api.basepath", (String)null);
        this.title = getInitParameter(servletConfig, "swagger.api.title", "");
        this.filterClass = getInitParameter(servletConfig, "swagger.filter", (String)null);
        if (this.basePath != null) {
            String[] parts = this.basePath.split("://");
            if (parts.length > 1) {
                this.scheme = Scheme.forValue(parts[0]);
                int pos = parts[1].indexOf("/");
                if (pos >= 0) {
                    this.basePath = parts[1].substring(pos);
                    this.host = parts[1].substring(0, pos);
                } else {
                    this.basePath = null;
                    this.host = parts[1];
                }
            }
        }

        if (this.filterClass != null) {
            try {
                SwaggerSpecFilter filter = (SwaggerSpecFilter)Class.forName(this.filterClass).newInstance();
                if (filter != null) {
                    FilterFactory.setFilter(filter);
                }
            } catch (Exception var4) {
                LOGGER.error("failed to load filter", var4);
            }
        }

    }

    private static String getInitParameter(HashMap<String,String> servletConfig, String parameterName, String defaultValue) {
        String value = servletConfig.get(parameterName);
        return StringUtils.isEmpty(value) ? defaultValue : value;
    }

    public Swagger configure(Swagger swagger) {
        if (swagger == null) {
            return null;
        } else {
            if (StringUtils.isNotBlank(this.basePath)) {
                swagger.basePath(this.basePath);
            }

            if (StringUtils.isNotBlank(this.host)) {
                swagger.host(this.host);
            }

            if (this.scheme != null) {
                swagger.scheme(this.scheme);
            }

            Info info = swagger.getInfo();
            if (info == null) {
                info = new Info();
                swagger.info(info);
            }

            if (StringUtils.isNotBlank(this.title)) {
                info.title(this.title);
            }

            if (StringUtils.isNotBlank(this.apiVersion)) {
                info.version(this.apiVersion);
            }

            return swagger;
        }
    }

    public String getFilterClass() {
        return this.filterClass;
    }
}

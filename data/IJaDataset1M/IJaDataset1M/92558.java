package org.embedded.tomcat.config;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.embedded.tomcat.utils.CustomToStringStyle;

public class Servlet {

    String name;

    String clazz;

    int loadOnStartup;

    InitParameter initParams;

    ServletMapping servletMappings;

    public String getName() {
        return name;
    }

    public String getClazz() {
        return clazz;
    }

    public int getLoadOnStartup() {
        return loadOnStartup;
    }

    public InitParameter getInitParams() {
        return initParams;
    }

    public ServletMapping getServletMappings() {
        return servletMappings;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, new CustomToStringStyle(3));
    }
}

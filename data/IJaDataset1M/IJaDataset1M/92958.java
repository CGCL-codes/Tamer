package com.quikj.server.app;

/**
 * @author amit
 *
 */
public interface EndPointManagementMBean {

    public static final String MBEAN_NAME = "com.quikj.server.app:type=EndPointManagement";

    public void setTrace(String enabled);

    public String getTrace();

    public void setTraceMessage(String enabled);

    public String getTraceMessage();

    public String getEndpointCount();
}

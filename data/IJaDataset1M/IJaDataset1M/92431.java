package org.monet.reportgenerator.server.configuration;

public interface Configuration {

    public static final String REPORTSERVICE_URL = "REPORTSERVICE.URL";

    public static final String DATASOURCENAME = "DATASOURCENAME";

    public static final String DBTYPE = "DATABASE.TYPE";

    String getTemplateDir();

    String getUrl();

    String getDocServerUrl();

    String getReportServiceUrl();

    String getProperty(String key);
}

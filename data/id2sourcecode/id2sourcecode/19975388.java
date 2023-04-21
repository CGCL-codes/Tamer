    public DefaultReportingTransactionFactory(Map config) throws ReportingException {
        Object location = config.get(LOCATION_KEY);
        InputStream is = null;
        try {
            if (location instanceof String) {
                URL url = new URL((String) location);
                is = url.openStream();
            } else if (location instanceof URL) {
                is = ((URL) location).openStream();
            } else if (location instanceof InputStream) {
                is = (InputStream) location;
            } else {
                LOGGER.warn("invalid-url", location);
                LOGGER.warn("reporting-disabled");
            }
        } catch (MalformedURLException e) {
            LOGGER.warn("invalid-url", location);
            LOGGER.warn("reporting-disabled");
        } catch (IOException e) {
            LOGGER.warn("configuration-file-not-found", location);
            LOGGER.warn("reporting-disabled");
        }
        ReportingConfig reportingConfig = null;
        if (is != null) {
            try {
                JibxReportingConfigParser parser = new JibxReportingConfigParser();
                reportingConfig = parser.parse(is);
            } catch (RuntimeException re) {
                LOGGER.fatal("failed-to-parse-configuration-file", location, re);
                throw new ReportingException("failed-to-parse-configuration-file", location);
            }
        }
        FACTORY = new MetricGroupProxyFactory(reportingConfig);
    }

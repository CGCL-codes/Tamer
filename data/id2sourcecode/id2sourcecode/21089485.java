    private synchronized void loadGlobalSettings() {
        String propFileName = System.getProperty(MapperConstants.CONFIG_FILE_SYS_PROP);
        if (MappingUtils.isBlankOrNull(propFileName)) {
            propFileName = MapperConstants.DEFAULT_CONFIG_FILE;
        }
        InitLogger.log(log, "Trying to find Dozer configuration file: " + propFileName);
        ResourceLoader loader = new ResourceLoader();
        URL url = loader.getResource(propFileName);
        if (url == null) {
            InitLogger.log(log, "Dozer configuration file not found: " + propFileName + ".  Using defaults for all Dozer global properties.");
            return;
        } else {
            InitLogger.log(log, "Using URL [" + url + "] for Dozer global property configuration");
        }
        Properties props = new Properties();
        try {
            InitLogger.log(log, "Reading Dozer properties from URL [" + url + "]");
            props.load(url.openStream());
        } catch (IOException e) {
            MappingUtils.throwMappingException("Problem loading Dozer properties from URL [" + propFileName + "]", e);
        }
        String propValue = props.getProperty(PropertyConstants.STATISTICS_ENABLED);
        if (propValue != null) {
            statisticsEnabled = Boolean.valueOf(propValue).booleanValue();
        }
        propValue = props.getProperty(PropertyConstants.CONVERTER_CACHE_MAX_SIZE);
        if (propValue != null) {
            converterByDestTypeCacheMaxSize = Long.parseLong(propValue);
        }
        propValue = props.getProperty(PropertyConstants.SUPERTYPE_CACHE_MAX_SIZE);
        if (propValue != null) {
            superTypesCacheMaxSize = Long.parseLong(propValue);
        }
        propValue = props.getProperty(PropertyConstants.AUTOREGISTER_JMX_BEANS);
        if (propValue != null) {
            autoregisterJMXBeans = Boolean.valueOf(propValue).booleanValue();
        }
        loadedByFileName = propFileName;
        InitLogger.log(log, "Finished configuring Dozer global properties");
    }

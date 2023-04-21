    public static Properties getProperties(String propertyFilename) {
        boolean loadLog4jFile = (propertyFilename.indexOf("log4j") >= 0) ? true : false;
        Properties properties = null;
        try {
            properties = (Properties) filename_properties_map.get(propertyFilename);
            if (properties == null) {
                URL url = FileUtil.toURL(propertyFilename);
                if (AppState.isVerbose()) System.out.println(propertyFilename + "--> url: " + url);
                if (!loadLog4jFile) log.info(propertyFilename + "--> url: " + url);
                if (url != null) {
                    properties = new Properties();
                    InputStream in = url.openStream();
                    properties.load(in);
                    in.close();
                    url = null;
                    filename_properties_map.put(propertyFilename, properties);
                } else {
                    if (AppState.isVerbose()) System.err.println("warning: " + propertyFilename + " not found");
                    if (!loadLog4jFile) log.error(propertyFilename + " not found");
                }
                if (!loadLog4jFile) log.info("load " + propertyFilename + " succeeded");
            }
        } catch (IOException e) {
            System.err.println("error reading " + propertyFilename + " " + e);
            if (!loadLog4jFile) log.error("error reading " + propertyFilename + " " + e);
        }
        return properties;
    }

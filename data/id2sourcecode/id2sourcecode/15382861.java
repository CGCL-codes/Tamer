    private void loadProperties() {
        try {
            ClassLoader loader = PropertiesManager.class.getClassLoader();
            if (loader == null) loader = ClassLoader.getSystemClassLoader();
            String propFile = "application.properties";
            java.net.URL url = loader.getResource(propFile);
            try {
                prop.load(url.openStream());
            } catch (Exception e) {
                log.error("Could not load configuration file: " + propFile);
            }
        } catch (Exception e) {
            log.error("ERROR in loading properties file.");
            e.printStackTrace();
        }
    }

    private void loadProperties() {
        properties = new Properties();
        try {
            String p = getClass().getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
            path = "/" + p.substring(0, p.lastIndexOf("/"));
            java.io.File propfile = new java.io.File(path + "/" + propertiesFilename);
            if (propfile.exists()) {
                log.debug("PropertiesLoader() Trying to load properties file '" + propertiesFilename + "' from directory path:" + path);
                java.io.FileInputStream fis = new java.io.FileInputStream(propfile);
                properties.load(fis);
            } else {
                log.debug("PropertiesLoader() Properties file not in directory path:'" + path + "' Loading properties file '" + propertiesFilename + "' from classpath");
                URL url = ClassLoader.getSystemResource(propertiesFilename);
                properties.load(url.openStream());
            }
            if (properties.getProperty("opennmsUrl") != null) {
                opennmsUrl = properties.getProperty("opennmsUrl");
            } else log.error("PropertiesLoader() 'opennmsUrl' property does not exist in '" + propertiesFilename + "' file. Using default value.");
            if (properties.getProperty("username") != null) {
                username = properties.getProperty("username");
            } else log.error("PropertiesLoader() 'username' property does not exist in '" + propertiesFilename + "' file. Using default value.");
            if (properties.getProperty("password") != null) {
                password = properties.getProperty("password");
            } else log.error("PropertiesLoader() 'password' property does not exist in '" + propertiesFilename + "' file. Using default value.");
            if (properties.getProperty("foreign_source") != null) {
                foreign_source = properties.getProperty("foreign_source");
            } else log.error("PropertiesLoader() 'foreign_source' property does not exist in '" + propertiesFilename + "' file. Using default value.");
        } catch (Throwable e) {
            log.error("PropertiesLoader() unable to load '" + propertiesFilename + "' file from classpath or file path '" + path + "'. Using default properties. ");
        }
        log.info("PropertiesLoader() using properties: foreign_source='" + foreign_source + "', opennmsUrl='" + opennmsUrl + "', username='" + username + "', password='" + password + "'");
    }

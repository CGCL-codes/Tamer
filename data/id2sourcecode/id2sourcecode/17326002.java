    static Properties loadProperties(String defaultsName, String propertiesName, Logger log) throws IOException {
        Properties bundle = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL defaultUrl = null;
        URL url = null;
        if (loader instanceof URLClassLoader) {
            URLClassLoader ucl = (URLClassLoader) loader;
            defaultUrl = ucl.findResource(defaultsName);
            url = ucl.findResource(propertiesName);
            log.trace("findResource: " + url);
        }
        if (defaultUrl == null) defaultUrl = loader.getResource(defaultsName);
        if (url == null) url = loader.getResource(propertiesName);
        if (url == null && defaultUrl == null) {
            String msg = "No properties file: " + propertiesName + " or defaults: " + defaultsName + " found";
            throw new IOException(msg);
        }
        log.trace("Properties file=" + url + ", defaults=" + defaultUrl);
        Properties defaults = new Properties();
        if (defaultUrl != null) {
            try {
                InputStream is = defaultUrl.openStream();
                defaults.load(is);
                is.close();
                log.debug("Loaded defaults, users=" + defaults.keySet());
            } catch (Throwable e) {
                log.debug("Failed to load defaults", e);
            }
        }
        bundle = new Properties(defaults);
        if (url != null) {
            InputStream is = url.openStream();
            if (is != null) {
                bundle.load(is);
                is.close();
            } else {
                throw new IOException("Properties file " + propertiesName + " not avilable");
            }
            log.debug("Loaded properties, users=" + bundle.keySet());
        }
        return bundle;
    }

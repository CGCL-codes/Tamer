    static Properties loadProperties(String propertiesName, Logger log) throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = null;
        if (loader instanceof URLClassLoader) {
            URLClassLoader ucl = (URLClassLoader) loader;
            url = ucl.findResource(propertiesName);
            log.trace("findResource: " + url);
        }
        if (url == null) url = loader.getResource(propertiesName);
        if (url == null) {
            url = new URL(propertiesName);
        }
        log.trace("Properties file=" + url);
        Properties bundle = new Properties();
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

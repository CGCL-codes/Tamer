    protected ImageReaderWriterFactory(String key, String def) {
        String val = System.getProperty(key, def);
        URL url;
        try {
            url = new URL(val);
        } catch (MalformedURLException e) {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl == null || (url = cl.getResource(val)) == null) {
                if ((url = ImageReaderWriterFactory.class.getClassLoader().getResource(val)) == null) {
                    throw new ConfigurationError("missing resource: " + val);
                }
            }
        }
        InputStream is = null;
        try {
            is = url.openStream();
            config.load(is);
        } catch (IOException e) {
            throw new ConfigurationError("failed to load imageio configuration from " + url, e);
        } finally {
            CloseUtils.safeClose(is);
        }
    }

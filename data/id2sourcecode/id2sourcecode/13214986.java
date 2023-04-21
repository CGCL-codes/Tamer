    public static void initProperties() throws IOException {
        final File dir = getDbDir();
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
        final String p = "JaxmasRegistryTestCase.properties";
        final URL url = Thread.currentThread().getContextClassLoader().getResource(p);
        if (url == null) {
            throw new IllegalStateException("Missing resource file: " + p);
        }
        properties = new Properties();
        properties.load(url.openStream());
    }

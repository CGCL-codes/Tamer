    private final Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        ClassLoader loader = NameMatcherPropertiesLoaderImpl.class.getClassLoader();
        URL url = loader.getResource(PROPERTIES_FILENAME);
        InputStream stream = url.openStream();
        try {
            properties.load(stream);
        } finally {
            stream.close();
        }
        return properties;
    }

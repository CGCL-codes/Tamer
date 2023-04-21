    private static Properties loadProperties() throws Exception {
        Properties properties = new Properties();
        ClassLoader loader = SegmentModule.class.getClassLoader();
        URL url = loader.getResource("wordseg.properties");
        properties.load(url.openStream());
        return properties;
    }

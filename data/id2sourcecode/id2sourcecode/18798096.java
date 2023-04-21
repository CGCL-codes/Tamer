    private Properties loadProperties() throws Exception {
        Properties properties = new Properties();
        URL url = getClass().getResource("/evemanage.properties");
        properties.load(url.openStream());
        return properties;
    }

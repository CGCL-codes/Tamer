    public static Properties loadProperties(String string) {
        Properties properties = new Properties();
        URL url = ResourceUtils.getResource(string);
        try {
            properties.load(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

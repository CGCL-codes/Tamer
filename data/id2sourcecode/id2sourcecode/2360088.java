    public static Properties loadProperties(URL url) throws IOException {
        return loadProperties(url.openStream());
    }

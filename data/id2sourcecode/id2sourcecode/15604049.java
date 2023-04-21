    public static Properties readProperties(URL url) throws IOException {
        Validations.notNull(url, "URL");
        return PropertiesReader.readPropertiesAndClose(url.openStream());
    }

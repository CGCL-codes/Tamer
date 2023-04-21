    public static Properties getAsProperties(final String name) throws IOException {
        Properties props = new Properties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource(name);
        if (url != null) {
            try {
                props.loadFromXML(url.openStream());
            } catch (InvalidPropertiesFormatException e) {
                props.load(url.openStream());
            }
        } else {
            try {
                props.loadFromXML(new FileInputStream(name));
            } catch (InvalidPropertiesFormatException e) {
                props.load(new FileInputStream(name));
            }
        }
        return props;
    }

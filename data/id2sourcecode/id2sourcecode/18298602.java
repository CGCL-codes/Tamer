    private void loadProperties(final String propertyFilename) {
        final URL url = ClassLoader.getSystemResource(propertyFilename);
        if (url == null) {
            throw new RuntimeException("Couldn't find properties in classpath: \"" + propertyFilename + "\"");
        }
        properties = new Properties();
        try {
            InputStream s = url.openStream();
            properties.load(s);
            s.close();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load properties from URL " + url);
        }
    }

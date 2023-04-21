    public static Properties loadProperties(final String packagePath) {
        final Properties properties = new Properties();
        final URL url = ResourceUtils.getResource(packagePath);
        if (null != url) {
            try {
                properties.load(url.openStream());
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return properties;
    }

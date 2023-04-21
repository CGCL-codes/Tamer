    public static InputStream loadResource(final String name) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource(name);
        if (url != null) {
            return url.openStream();
        }
        return new FileInputStream(name);
    }

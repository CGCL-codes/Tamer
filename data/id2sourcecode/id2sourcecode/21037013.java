    public static InputStream systemClasspathStream(String path) {
        InputStream in = null;
        URL url = ClassLoader.getSystemResource(path);
        if (url != null) {
            try {
                in = url.openStream();
            } catch (IOException e) {
                log.warn("Cannot open system classpath stream " + path, e);
            }
        }
        return in;
    }

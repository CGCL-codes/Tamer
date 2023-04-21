    private InputStream classpathStream(String path) {
        InputStream in = null;
        URL url = getClass().getClassLoader().getResource(path);
        if (url != null) {
            try {
                in = url.openStream();
            } catch (IOException e) {
                log.warn("Cant open classpath input stream " + path, e);
            }
        }
        return in;
    }

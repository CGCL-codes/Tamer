    public InputStream classpathStream(String path) {
        InputStream in = null;
        URL url = getClass().getClassLoader().getResource(path);
        if (url != null) {
            try {
                in = url.openStream();
            } catch (IOException e) {
                log.warn(e);
            }
        }
        return in;
    }

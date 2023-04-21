    public static Configuration readConfiguration(URL url) {
        try {
            InputStream stream = url.openStream();
            return readConfiguration(stream);
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't open stream to URL");
        }
    }

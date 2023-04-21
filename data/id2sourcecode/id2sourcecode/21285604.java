    public static final InputStream getSystemFileInputStream(final String fileName) {
        final URL url = getResource(fileName);
        try {
            return url.openStream();
        } catch (final IOException ioe) {
            throw new RuntimeException("Failure when loading file in classpath : " + fileName, ioe);
        }
    }

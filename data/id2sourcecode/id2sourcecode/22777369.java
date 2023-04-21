    public static final InputStream openStream(URL url) {
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new CustomRuntimeException(e);
        }
    }

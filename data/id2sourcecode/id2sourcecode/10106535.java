    public static String getUrlContent(final URL url) throws IOException {
        return getInputStreamContent(url.openStream());
    }

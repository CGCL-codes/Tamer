    public static StringBuilder getContent(final URL url) throws IOException {
        return getContent(url.openStream());
    }

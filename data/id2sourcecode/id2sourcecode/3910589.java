    public static Reader getURLReader(URL url) throws IOException {
        final InputStream fs = url.openStream();
        return new InputStreamReader(fs);
    }

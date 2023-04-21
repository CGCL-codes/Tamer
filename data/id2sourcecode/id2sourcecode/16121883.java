    public static InputStream openStreamFromUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        return openStreamFromUrl(url);
    }

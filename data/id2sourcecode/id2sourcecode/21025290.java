    protected URLConnection openConnection(final URL url) throws IOException {
        return new BlockURLConnection(url);
    }

    protected URLConnection openConnection(final URL url) throws IOException {
        final URL proxyURL = new URL(url.toExternalForm());
        return proxyURL.openConnection();
    }

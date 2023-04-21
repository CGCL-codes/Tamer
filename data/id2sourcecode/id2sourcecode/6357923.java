    public HttpURLConnection openConnection(URL url) throws IOException {
        if (!url.getProtocol().startsWith("http")) {
            throw new IllegalArgumentException("Not an HTTP url: " + url);
        }
        return (HttpURLConnection) url.openConnection();
    }

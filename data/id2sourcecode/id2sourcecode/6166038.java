    public URLConnection openConnection() throws IOException {
        URL url = new URL(baseUrl);
        return url.openConnection();
    }

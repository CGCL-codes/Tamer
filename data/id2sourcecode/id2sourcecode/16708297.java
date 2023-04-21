    public InputStream openFileStream(String filename) throws IOException {
        URL url;
        try {
            url = new URL(filename);
        } catch (java.net.MalformedURLException mue) {
            throw new IOException("Invalid URL " + filename);
        }
        return url.openStream();
    }

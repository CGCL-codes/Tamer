    public InputStream openStream() throws IOException {
        InputStream stream;
        try {
            stream = new URL(url).openStream();
        } catch (IOException e) {
            stream = null;
        }
        return stream;
    }

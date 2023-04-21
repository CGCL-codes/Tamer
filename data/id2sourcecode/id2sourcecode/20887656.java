    private void initialize() throws IOException {
        if (iszip) {
            InputStream is = url.openStream();
            init(is);
            is.close();
        }
    }

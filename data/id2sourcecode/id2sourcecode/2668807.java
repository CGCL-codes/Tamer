    public void loadFrom(URL url) throws IOException {
        InputStream stream = url.openStream();
        load(stream);
    }

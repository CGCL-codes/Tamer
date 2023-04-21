    public void putFile(String path, URL url) throws IOException {
        InputStream is = url.openStream();
        putFile(path, is);
    }

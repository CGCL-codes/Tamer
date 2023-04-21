    void add(URL url, String relPath) throws IOException {
        add(url.openStream(), relPath);
    }

    public InputStream getInputStream(URL url) throws IOException {
        return getInputStream(null, url.openConnection());
    }

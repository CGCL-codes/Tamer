    public static InputStream getResourceAsStream(String path, Class yourClass) throws IOException {
        URL url = getResource(path, yourClass);
        if (url == null) throw new IOException("Can't find resource: " + path);
        return url.openStream();
    }

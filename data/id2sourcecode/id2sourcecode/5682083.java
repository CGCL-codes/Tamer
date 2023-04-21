    public static InputStream getResourceAsStream(String path, String extension) {
        URL url = getResource(path, extension);
        return URLUtil.openStream(url);
    }

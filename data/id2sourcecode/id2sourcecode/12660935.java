    public static InputStream getInputStream(URL url) throws IOException {
        if (url == null) return null;
        try {
            return url.openStream();
        } catch (FileNotFoundException e) {
            return null;
        }
    }

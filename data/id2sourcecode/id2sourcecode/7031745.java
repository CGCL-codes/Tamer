    public static InputSource getDocument(URL url) throws IOException {
        return getDocument(url.openStream());
    }

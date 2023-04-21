    public static InputStream getDocumentAsInputStream(URL url) throws IOException {
        InputStream in = url.openStream();
        return in;
    }

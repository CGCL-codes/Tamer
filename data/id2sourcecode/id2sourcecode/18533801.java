    public static PDDocument newDocument(URL url) throws Exception {
        return newDocument(url.openStream());
    }

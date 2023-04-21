    public static Document createDocument(String uri) throws Exception {
        URL url = new URL(uri);
        return builder.parse(url.openStream());
    }

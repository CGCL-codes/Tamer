    public static Document createDocument(URL url) throws Exception {
        return builder.parse(url.openStream());
    }

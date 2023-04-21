    public static Document createDocument(String uri) throws Exception {
        URL url = new URL(uri);
        synchronized (lock) {
            return builder.parse(url.openStream());
        }
    }

    public static Document createDocument(URL url) throws Exception {
        synchronized (lock) {
            return builder.parse(url.openStream());
        }
    }

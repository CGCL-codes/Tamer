    private static Resource createResource(URL url) throws IOException {
        if (url == null) {
            return null;
        }
        InputStream stream = url.openStream();
        if (stream.read() == -1) {
            return null;
        }
        stream.close();
        return new Resource(url);
    }

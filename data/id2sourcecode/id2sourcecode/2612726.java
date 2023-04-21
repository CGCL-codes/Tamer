    public static List readConfiguration(URL url, URI base) throws IOException {
        InputStream stream = null;
        try {
            stream = url.openStream();
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) return Collections.EMPTY_LIST;
            throw e;
        }
        try {
            return readConfiguration(stream, base);
        } finally {
            stream.close();
        }
    }

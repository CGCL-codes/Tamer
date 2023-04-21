    public static byte[] loadBinaryFile(URL url) throws IOException {
        return loadBinaryFile(url.openStream());
    }

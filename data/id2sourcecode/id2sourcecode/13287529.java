    private static byte[] getData(URL url) throws IOException {
        return getData(url.openStream());
    }

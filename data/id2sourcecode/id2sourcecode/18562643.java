    public static Properties loadProperties(URL url) throws IOException {
        byte[] cached = (byte[]) urlCache.get(url);
        InputStream in = cached == null ? url.openStream() : new ByteArrayInputStream(cached);
        return loadProperties(in);
    }

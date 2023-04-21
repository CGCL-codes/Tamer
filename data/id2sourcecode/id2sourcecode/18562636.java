    public static String loadText(URL url) throws IOException {
        byte[] cached = (byte[]) urlCache.get(url);
        InputStream in = cached == null ? url.openStream() : new ByteArrayInputStream(cached);
        String text = loadText(in);
        in.close();
        return text;
    }

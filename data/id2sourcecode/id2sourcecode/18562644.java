    public static void cacheURL(URL url) throws IOException {
        InputStream in = url.openStream();
        byte[] data = readToEnd(in);
        in.close();
        urlCache.put(url, data);
    }

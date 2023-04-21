    private InputStream getCached(String url) throws MalformedURLException, IOException {
        if (cache == null) {
            return new URL(url).openStream();
        }
        if (!cache.exists()) cache.mkdirs();
        File c = new File(cache, URLEncoder.encode(url, "UTF-8"));
        if (c.exists()) return new FileInputStream(c);
        return new URL(url).openStream();
    }

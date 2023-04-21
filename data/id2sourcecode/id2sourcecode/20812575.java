    public int loadCache(URL url) throws IOException {
        return loadCache(new BufferedReader(new InputStreamReader(url.openStream())));
    }

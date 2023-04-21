    public synchronized URLConnection openConnection(URL url, Proxy p) throws IOException {
        return new URL2ByteCacheConnection(url);
    }

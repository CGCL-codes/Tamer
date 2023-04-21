    public static URLConnection openConnectionForceNoProxy(URL url) throws IOException {
        return url.openConnection(Proxy.NO_PROXY);
    }

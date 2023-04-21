    public static final Closeable openURL(final URL url, final int maxTimeout, final boolean forWrite) throws IOException {
        if (null == url) return null;
        final URLConnection conn = url.openConnection();
        if (maxTimeout > 0) {
            conn.setConnectTimeout(maxTimeout);
            conn.setReadTimeout(maxTimeout);
        }
        if (forWrite) return conn.getOutputStream(); else return conn.getInputStream();
    }

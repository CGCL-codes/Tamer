    private URLConnection prepareRemoteConnection(final String srcPath) throws IOException {
        final URL url = new URL(srcPath);
        final URLConnection conn = url.openConnection();
        conn.setAllowUserInteraction(false);
        conn.setConnectTimeout(getConnectTimeout());
        conn.setReadTimeout(getReadTimeout());
        return conn;
    }

    protected long doGetLastModifiedTime() throws Exception {
        final URLConnection conn = url.openConnection();
        final InputStream in = conn.getInputStream();
        try {
            return conn.getLastModified();
        } finally {
            in.close();
        }
    }

    protected long doGetContentSize() throws Exception {
        final URLConnection conn = url.openConnection();
        final InputStream in = conn.getInputStream();
        try {
            return conn.getContentLength();
        } finally {
            in.close();
        }
    }

    public int length(String key) {
        if (logger.isDebugEnabled()) logger.debug("length(" + key + ")");
        URL url = getURL(key);
        try {
            URLConnection conn = url.openConnection();
            if (conn instanceof HttpURLConnection) ((HttpURLConnection) conn).setRequestMethod("HEAD");
            try {
                return conn.getContentLength();
            } finally {
                conn.getInputStream().close();
            }
        } catch (IOException e) {
            throw new ResourceNotFoundException("Cannot connect to URL " + getURL(key));
        }
    }

    public long lastModifiedImpl(String key) {
        URL url = getURL(key);
        try {
            URLConnection conn = url.openConnection();
            if (conn instanceof HttpURLConnection) ((HttpURLConnection) conn).setRequestMethod("HEAD");
            try {
                return conn.getLastModified();
            } finally {
                conn.getInputStream().close();
            }
        } catch (IOException e) {
            throw new ResourceNotFoundException("Cannot connect to URL " + url);
        }
    }

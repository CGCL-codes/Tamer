    protected InputStream getInputStream(URL url) {
        try {
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(3000);
            return conn.getInputStream();
        } catch (IOException e) {
            Log.e(Constants.LOG_TAG, "Error getting input stream for url: " + url, e);
            return null;
        }
    }

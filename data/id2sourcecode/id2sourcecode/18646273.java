    public static URLConnection makeURLConnection(URL url, int timeout) {
        URLConnection conn = null;
        try {
            conn = url.openConnection();
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
        } catch (Exception ex) {
        }
        return conn;
    }

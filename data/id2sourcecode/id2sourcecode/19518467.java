    public static HttpURLConnection createConnection(URL url) {
        try {
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(7000);
            return conn;
        } catch (Exception e) {
            return null;
        }
    }

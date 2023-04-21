    public static HttpURLConnection openConnection(URL url, String username, String password) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        setBasicAuthIfNeeded(conn, username, password);
        return conn;
    }

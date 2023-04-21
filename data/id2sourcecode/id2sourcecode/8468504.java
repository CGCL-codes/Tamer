    private HttpURLConnection getConnection(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
        conn.setRequestProperty("User-Agent", ApplicationProperty.USER_AGENT);
        conn.connect();
        return conn;
    }

    private static HttpURLConnection getConnection(String servlet, HashMap<String, String> queryParameters) throws MalformedURLException, IOException {
        URL url = getURL(servlet, queryParameters);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(getDefaultConnectTimeout());
        conn.setReadTimeout(getDefaultReadTimeout());
        _openedConnections.add(conn);
        return conn;
    }

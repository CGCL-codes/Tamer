    protected HttpURLConnection createGetConnectionFor(URL url) throws IOException {
        HttpURLConnection conn = HttpTransportManager.openConnection(url, getBasicAuthUsername(), getBasicAuthPassword());
        conn.setRequestProperty("accept-encoding", "gzip");
        conn.setConnectTimeout(getHttpTimeOutInMs());
        conn.setReadTimeout(getHttpTimeOutInMs());
        conn.setRequestMethod("GET");
        return conn;
    }

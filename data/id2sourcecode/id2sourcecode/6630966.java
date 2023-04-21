    private static HttpURLConnection getConnection(URL url, String method, String ctype) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Accept", "text/xml,text/javascript,text/html");
        conn.setRequestProperty("User-Agent", "Top4Java");
        conn.setRequestProperty("Content-Type", ctype);
        return conn;
    }

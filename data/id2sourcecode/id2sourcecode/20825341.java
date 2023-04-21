    private HttpURLConnection openConnection(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        for (Map.Entry<String, String> ent : headers.entrySet()) conn.setRequestProperty(ent.getKey(), ent.getValue());
        return conn;
    }

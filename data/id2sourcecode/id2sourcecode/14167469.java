    static HttpURLConnection getConnection(String url, Map<?, ?> headers) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        if (headers != null) {
            for (Map.Entry<?, ?> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        return connection;
    }

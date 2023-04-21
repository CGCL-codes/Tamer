    protected HttpURLConnection createConnection() throws Exception {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if (requestHeaders != null) {
            for (Entry<String, String> entry : requestHeaders.entrySet()) {
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        return connection;
    }

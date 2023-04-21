    private static URLConnection openConnection(URL url, Map<String, String> headers) throws IOException {
        final URLConnection connection = url.openConnection();
        connection.setUseCaches(false);
        if (CONNECTION_TIMEOUT > 0) {
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
        }
        if (READ_TIMEOUT > 0) {
            connection.setReadTimeout(READ_TIMEOUT);
        }
        connection.setRequestProperty("Accept-Encoding", "gzip");
        if (headers != null) {
            for (final Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        return connection;
    }

    static HttpURLConnection getConnection(String url, Iterable<Parameter> headers) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        if (headers != null) {
            for (Parameter h : headers) connection.setRequestProperty(h.getKey(), h.getValue());
        }
        return connection;
    }

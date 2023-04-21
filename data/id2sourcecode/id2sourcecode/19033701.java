    protected HttpURLConnection makeRequest(String method, String resource, Map headers) throws MalformedURLException, IOException {
        URL url = makeURL(resource);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        addHeaders(connection, headers);
        addAuthHeader(connection, method, resource);
        return connection;
    }

    private HttpURLConnection makeRequest(String method, String resource, Map headers, S3Object object) throws IOException {
        URL url = makeURL(resource);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        addHeaders(connection, headers);
        if (object != null) addMetadataHeaders(connection, object.metadata);
        addAuthHeader(connection, method, resource);
        return connection;
    }

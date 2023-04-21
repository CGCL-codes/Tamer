    private HttpURLConnection makeRequest(String method, String bucket, String key, Map<String, String> pathArgs, Map<String, List<String>> headers, Map<String, List<String>> metadata) throws MalformedURLException, IOException {
        URL url = this.callingFormat.getURL(this.isSecure, server, this.port, bucket, key, pathArgs);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        if (!connection.getInstanceFollowRedirects() && callingFormat.supportsLocatedBuckets()) throw new RuntimeException("HTTP redirect support required.");
        addHeaders(connection, headers);
        if (metadata != null) {
            addMetadataHeaders(connection, metadata);
        }
        addAuthHeader(connection, method, bucket, key, pathArgs);
        return connection;
    }

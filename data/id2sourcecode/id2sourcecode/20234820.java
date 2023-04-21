    private HttpURLConnection makeRequest(String method, String bucket, String key, Map pathArgs, Map headers, S3Object object) throws MalformedURLException, IOException {
        URL url = this.callingFormat.getURL(this.isSecure, server, this.port, bucket, key, pathArgs);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if (object != null && object.getSize() > 0) {
            int size = (int) object.getSize();
            if (target.isEncrypted()) {
                size += 16 - (size % 16);
            }
            connection.setFixedLengthStreamingMode(size);
        }
        int timeout = ConfigManager.getConfigManager().getPropertyInt(BonkeyConstants.S3_TIMEOUT);
        connection.setConnectTimeout(timeout * 1000);
        connection.setRequestMethod(method);
        if (!connection.getInstanceFollowRedirects() && callingFormat.supportsLocatedBuckets()) throw new RuntimeException(Messages.getString("AWSAuthConnection.ErrorHTTPRedirect"));
        addHeaders(connection, initHeaders);
        addHeaders(connection, headers);
        if (object != null) addMetadataHeaders(connection, object.metadata);
        addAuthHeader(connection, method, bucket, key, pathArgs);
        return connection;
    }

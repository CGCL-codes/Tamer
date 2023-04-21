    public String request(final String uri, final String requestMethod, final String content, final Map<String, List<Object>> requestProperties, final int connectionTimeout, final int readTimeout) throws IOException {
        LogHelper.logMethod(log, toObjectString(), "request(), uri = " + uri + ", requestMethod = " + requestMethod + ", content = " + content + ", requestProperties = " + requestProperties + ", connectionTimeout = " + connectionTimeout + ", readTimeout = " + readTimeout);
        final URL url = new URL(uri == null ? defaultURI : uri);
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestMethod == null ? defaultRequestMethod == null ? "GET" : defaultRequestMethod : requestMethod);
        if (requestProperties != null) {
            for (final Map.Entry<String, List<Object>> entry : requestProperties.entrySet()) {
                for (final Object value : entry.getValue()) {
                    connection.addRequestProperty(entry.getKey(), value.toString());
                }
            }
        }
        connection.setConnectTimeout(connectionTimeout == -1 ? defaultConnectionTimeout : connectionTimeout);
        connection.setReadTimeout(readTimeout == -1 ? defaultReadTimeout : readTimeout);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        if (content != null) {
            writeContent(content, connection.getOutputStream());
        }
        connection.connect();
        return readContent(connection.getInputStream());
    }

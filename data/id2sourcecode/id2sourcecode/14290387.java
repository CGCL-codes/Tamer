    private HttpURLConnection initHttpConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        addAuthenticationIfNeeded(connection);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
        connection.setRequestProperty("Accept", "*/*");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        customizeHttpConnectionBeforeCall(connection);
        return connection;
    }

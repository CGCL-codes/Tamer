    private HttpURLConnection getHttpConnection(String function, String options, String sessionId) throws IOException {
        StringBuilder sb = new StringBuilder();
        String spec = sb.append('/').append(API_VERSION).append('/').append(function).append(options).toString();
        URL url = new URL(_url, spec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        SslUtils.configureTrustedCertificate(connection, BCCAPI_COM_SSL_THUMBPRINT);
        if (sessionId != null) {
            connection.setRequestProperty("sessionId", sessionId);
        }
        connection.setReadTimeout(60000);
        return connection;
    }

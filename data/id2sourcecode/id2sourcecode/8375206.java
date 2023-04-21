    private HttpURLConnection getConnection(final String contentType) {
        log.trace("Entering getConnection");
        if (serverURL == null) {
            throw new IllegalStateException("Must set the server url first");
        }
        HttpURLConnection connection = null;
        try {
            URL url = new URL(serverURL);
            connection = (HttpURLConnection) url.openConnection();
            String encoding = new String(Base64.encodeBase64((merchantId + ":" + merchantKey).getBytes()));
            connection.setRequestProperty("Authorization", "Basic " + encoding);
            if (acceptAllCertificates) {
                if (connection instanceof HttpsURLConnection) {
                    HttpsURLConnection sslConnection = (HttpsURLConnection) connection;
                    sslConnection.setHostnameVerifier(new HostnameVerifier() {

                        public boolean verify(final String hostName, final SSLSession session) {
                            return true;
                        }
                    });
                }
            } else {
                if (clientSocketFactory != null) {
                    if (connection instanceof HttpsURLConnection) {
                        HttpsURLConnection sslConnection = (HttpsURLConnection) connection;
                        sslConnection.setSSLSocketFactory(clientSocketFactory);
                    }
                }
            }
            connection.setDoOutput(true);
            if (contentType != null) {
                connection.setRequestProperty("Content-Type", contentType);
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to connect to server", e);
        }
        log.trace("Leaving getConnection");
        if (connection == null) {
            throw new RuntimeException("Attempted to return a null connection");
        }
        return connection;
    }

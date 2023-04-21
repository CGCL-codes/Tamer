    public void connect(SSLContext sslContext, String address) throws Exception {
        URL url = new URL(address);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        if (sslContext != null) {
            connection.setSSLSocketFactory(sslContext.getSocketFactory());
        }
        connection.connect();
        connection.disconnect();
    }

    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) super.openConnection(url);
        connection.setSSLSocketFactory(sslSocketFactory);
        connection.setHostnameVerifier(new HostnameVerifierImpl());
        return connection;
    }

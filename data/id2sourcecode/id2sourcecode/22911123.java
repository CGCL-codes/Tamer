    private static URLConnection getConnection(URL url) throws Exception {
        URLConnection connection = url.openConnection();
        if (connection instanceof HttpsURLConnection) {
            HttpsURLConnection conn = (HttpsURLConnection) connection;
            conn.setHostnameVerifier(VERIFY_ANY);
            if (TRUST_ANY != null) {
                conn.setSSLSocketFactory(TRUST_ANY);
            }
        }
        return connection;
    }

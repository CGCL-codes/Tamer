    private static HttpURLConnection getHTTPConnection(String urlString) throws Exception {
        trustAllHttpsCertificates();
        HostnameVerifier hv = new HostnameVerifier() {

            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
        URL urlStr = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) urlStr.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setAllowUserInteraction(true);
        conn.connect();
        return conn;
    }

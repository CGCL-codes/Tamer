    public HttpsURLConnection getConnection(String uri) throws NoSuchAlgorithmException, KeyManagementException, IOException {
        SSLContext sc = SSLContext.getInstance("SSLv3");
        TrustManager[] tma = { new HttpsConnection() };
        sc.init(null, tma, null);
        SSLSocketFactory ssf = sc.getSocketFactory();
        HttpsURLConnection.setDefaultSSLSocketFactory(ssf);
        HttpsURLConnection connection = null;
        uri = uri.replaceAll("^(https://|http://)", "");
        URL url = new URL("https://" + uri);
        connection = (HttpsURLConnection) url.openConnection();
        return connection;
    }

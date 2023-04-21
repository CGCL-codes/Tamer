    public static InputStream openStreamFromUrl(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        if (connection instanceof HttpsURLConnection) {
            SSLContext context;
            try {
                context = SSLContext.getInstance("TLS");
            } catch (NoSuchAlgorithmException e) {
                throw new ProgrammingException(ProgrammingException.MISSING_SSL_ALGORITHM, "Could not initialize an SSL context with 'TLS' algorithm.", e);
            }
            TrustAnyoneManager taom = new TrustAnyoneManager();
            try {
                context.init(null, new TrustManager[] { taom }, null);
            } catch (KeyManagementException e) {
                throw new ProgrammingException(ProgrammingException.UNKNOWN, "Could not initialize the SSL context.", e);
            }
            SSLSocketFactory sf = context.getSocketFactory();
            HttpsURLConnection ssl = (HttpsURLConnection) connection;
            HttpsURLConnection.setDefaultSSLSocketFactory(sf);
            ssl.setHostnameVerifier(new HostnameVerifier() {

                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        }
        return connection.getInputStream();
    }

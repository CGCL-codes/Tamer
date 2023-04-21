    public void testWebSSL() throws Throwable {
        URL url = new URL("https://phantom.naru.com:1280/pub");
        HttpsURLConnection httpsconnection = (HttpsURLConnection) url.openConnection();
        KeyManager[] km = null;
        TrustManager[] tm = { new X509TrustManager() {

            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        } };
        SSLContext sslcontext = SSLContext.getInstance("SSL");
        sslcontext.init(km, tm, new SecureRandom());
        httpsconnection.setSSLSocketFactory(sslcontext.getSocketFactory());
        InputStream is = httpsconnection.getInputStream();
        readInputStream(is);
        is.close();
    }

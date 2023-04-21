    public static URLConnection getConnection(URL url) throws IOException {
        URLConnection con = url.openConnection();
        log.trace("URL: \n" + debugURL(url));
        if (con instanceof HttpsURLConnection && ALLOW_UNTRUSTED_SSL) {
            HttpsURLConnection httpsCon = (HttpsURLConnection) con;
            log.warn("Creating un-trusted SSL connection to: " + url.toExternalForm());
            try {
                SSLContext sc = SSLContext.getInstance("SSL");
                TrustManager tmchain[] = new TrustManager[1];
                tmchain[0] = new TrustAll();
                sc.init(null, tmchain, new SecureRandom());
                httpsCon.setSSLSocketFactory(sc.getSocketFactory());
                httpsCon.setHostnameVerifier(new HostVerifyAll());
            } catch (GeneralSecurityException ex) {
                throw new IOException("Error trying to establish SSL Connection", ex);
            }
            con = httpsCon;
        }
        String userInfo = url.getUserInfo();
        if (userInfo != null) {
            log.info("Adding Auth Info: " + userInfo);
            String encodedAuth = (new BASE64Encoder()).encode(userInfo.getBytes());
            con.setRequestProperty("Authorization", "Basic " + encodedAuth);
        }
        return con;
    }

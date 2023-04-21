    public MRefund action(MRefund pf1, MRefund pf) {
        String str = getXml(pf1);
        try {
            KeyManagerFactory kmf;
            KeyStore ks;
            TrustManagerFactory tmf;
            SSLContext sslc;
            kmf = KeyManagerFactory.getInstance(ALGORITHM);
            ks = KeyStore.getInstance("JKS");
            ks.load(this.getClass().getClassLoader().getResourceAsStream(KEYSTORE_FILE), PASSWORD.toCharArray());
            kmf.init(ks, PASSWORD.toCharArray());
            tmf = TrustManagerFactory.getInstance(ALGORITHM);
            tmf.init(ks);
            X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
            SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
            sslc = SSLContext.getInstance("SSL");
            sslc.init(kmf.getKeyManagers(), new TrustManager[] { tm }, new java.security.SecureRandom());
            SocketFactory sf = sslc.getSocketFactory();
            String refundURL = getValue("pos_refund_url");
            URL url = new URL(refundURL);
            sun.misc.BASE64Encoder en = new sun.misc.BASE64Encoder();
            String merchantId = getValue("pos_refund_merchantId");
            String s = "Basic " + en.encode((merchantId + ":" + PASSWORD).getBytes());
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestProperty("Authorization", s);
            httpsURLConnection.setSSLSocketFactory((SSLSocketFactory) sf);
            post(httpsURLConnection, str, pf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pf;
    }

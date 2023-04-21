    public SecureStore(String keyStorePath, String keyStorePassword, String myurl) {
        try {
            System.out.println(keyStorePath + ", " + keyStorePassword);
            FileInputStream is = new FileInputStream(keyStorePath);
            KeyStore ksKeys = KeyStore.getInstance("pkcs12");
            ksKeys.load(is, keyStorePassword.toCharArray());
            KeyStore ksTrust = KeyStore.getInstance("jks");
            ksTrust.load(new FileInputStream(trustStore), trustStorePass.toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ksKeys, keyStorePassword.toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ksTrust);
            SSLContext sc = SSLContext.getInstance("SSLv3");
            sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            this.ssf = sc.getSocketFactory();
            URL url = new URL(myurl);
            this.httpsConn = (HttpsURLConnection) url.openConnection();
            this.httpsConn.setHostnameVerifier(new HostnameVerifier() {

                public boolean verify(String urlHost, SSLSession ssls) {
                    if (!urlHost.equals(ssls.getPeerHost())) {
                        System.out.println("Alert: SSL Host" + ssls.getPeerHost() + " does not match URL Host" + urlHost);
                    }
                    return true;
                }
            });
            this.httpsConn.setSSLSocketFactory(ssf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

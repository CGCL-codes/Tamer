    public static void importToKeystore(String keyStorePath, KeyStore ks, String host) throws Exception {
        SSLContext context = SSLContext.getInstance("TLS");
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);
        X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
        SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
        context.init(null, new TrustManager[] { tm }, null);
        SSLSocketFactory factory = context.getSocketFactory();
        SSLSocket socket = (SSLSocket) factory.createSocket(host, 443);
        socket.setSoTimeout(10000);
        try {
            System.out.println("Starting SSL handshake...");
            socket.startHandshake();
            socket.close();
            System.out.println();
            System.out.println("No errors, certificate is already trusted");
        } catch (SSLException e) {
            e.printStackTrace();
        }
        X509Certificate[] chain = tm.chain;
        if (chain == null) {
            System.out.println("Could not obtain server certificate chain");
            return;
        }
        System.out.println();
        System.out.println("Server sent " + chain.length + " certificate(s):");
        System.out.println();
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        for (int i = 0; i < chain.length; i++) {
            X509Certificate cert = chain[i];
            String alias = host + "-" + (i + 1);
            if (ks.getCertificate(alias) == null) {
                System.out.println(" " + (i + 1) + " Subject " + cert.getSubjectDN());
                System.out.println("   Issuer  " + cert.getIssuerDN());
                sha1.update(cert.getEncoded());
                System.out.println("   sha1    " + toHexString(sha1.digest()));
                md5.update(cert.getEncoded());
                System.out.println("   md5     " + toHexString(md5.digest()));
                System.out.println();
                ks.setCertificateEntry(alias, cert);
            }
        }
    }

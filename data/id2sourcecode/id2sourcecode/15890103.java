    private static void generateCertificate(String[] passphrases, String FilePath, String FileName) throws Exception {
        if (!FilePath.endsWith(AdempiereLBR.getFileSeparator())) FilePath += AdempiereLBR.getFileSeparator();
        for (String passphrase : passphrases) {
            String load = FilePath + FileName;
            File file = new File(load);
            if (!file.exists()) load = null;
            String[] conexao = passphrase.split(":");
            char[] store = ("changeit").toCharArray();
            String host = conexao[0];
            int port = (conexao.length == 1) ? 443 : Integer.parseInt(conexao[1]);
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream in = null;
            if (load != null) in = new FileInputStream(load);
            ks.load(in, store);
            if (in != null) in.close();
            SSLContext context = SSLContext.getInstance("TLS");
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);
            X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
            SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
            context.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory factory = context.getSocketFactory();
            System.out.println("Opening connection to " + host + ":" + port + "...");
            SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
            socket.setSoTimeout(10000);
            try {
                System.out.println("Starting SSL handshake...");
                socket.startHandshake();
                socket.close();
                System.out.println();
                System.out.println("No errors, certificate is already trusted");
            } catch (SSLException e) {
                System.out.println("Certificate chain needed");
            }
            X509Certificate[] chain = tm.chain;
            if (chain == null) {
                System.out.println("Could not obtain server certificate chain");
                log.log(Level.WARNING, "Could not obtain server certificate chain");
                return;
            }
            System.out.println();
            System.out.println("Server sent " + chain.length + " certificate(s):");
            System.out.println();
            MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            for (int i = 0; i < chain.length; i++) {
                X509Certificate cert = chain[i];
                System.out.println(" " + (i + 1) + " Subject " + cert.getSubjectDN());
                System.out.println("   Issuer  " + cert.getIssuerDN());
                sha1.update(cert.getEncoded());
                System.out.println("   sha1    " + toHexString(sha1.digest()));
                md5.update(cert.getEncoded());
                System.out.println("   md5     " + toHexString(md5.digest()));
                System.out.println();
            }
            System.out.println("Enter certificate to add to trusted keystore");
            int k = 0;
            X509Certificate cert = chain[k];
            String alias = host + "-" + (k + 1);
            ks.setCertificateEntry(alias, cert);
            OutputStream out = new FileOutputStream(FilePath + FileName);
            ks.store(out, store);
            out.close();
            System.out.println();
            System.out.println(cert);
            System.out.println();
            System.out.println("Added certificate to keystore " + FileName + " using alias '" + alias + "'");
        }
    }

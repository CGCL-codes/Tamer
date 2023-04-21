    public static void updateKeyStore(String host, X509Certificate[] chain) {
        LOG.info("updateKeyStore begin");
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            for (int i = 0; i < chain.length; i++) {
                X509Certificate cert = chain[i];
                LOG.info(" " + (i + 1) + " Subject " + cert.getSubjectDN());
                LOG.info("   Issuer  " + cert.getIssuerDN());
                sha1.update(cert.getEncoded());
                LOG.info("   sha1    " + toHexString(sha1.digest()));
                md5.update(cert.getEncoded());
                LOG.info("   md5     " + toHexString(md5.digest()));
                String alias = host + "-" + (i + 1);
                KeyStore ks = openDefaultKeyStore();
                ks.setCertificateEntry(alias, cert);
                OutputStream out = new FileOutputStream("src/main/resources/keystore.jks");
                ks.store(out, PASSWORD.toCharArray());
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            LOG.error("updateKeyStore exception", e);
        }
        LOG.info("updateKeyStore end");
    }

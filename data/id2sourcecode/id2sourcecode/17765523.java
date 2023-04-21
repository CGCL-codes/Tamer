    public static X509Certificate loadX509Certificate(File file) throws CertificateException, NoSuchProviderException, IOException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
        FileInputStream fin = new FileInputStream(file);
        Certificate cert;
        try {
            fin.getChannel().lock(0L, Long.MAX_VALUE, true);
            cert = cf.generateCertificate(fin);
        } finally {
            fin.close();
        }
        if (cert == null) {
            throw new NullPointerException("Unknown error: Certificate was " + "null");
        }
        if (!(cert instanceof X509Certificate)) {
            throw new IllegalArgumentException("this file is not an X.509 " + "certificate, it's a " + cert.getClass().getName());
        }
        return (X509Certificate) cert;
    }

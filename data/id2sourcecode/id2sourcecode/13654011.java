    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, KeyStoreException, CertificateException, FileNotFoundException, IOException, InvalidKeyException, SecurityException, SignatureException {
        String storePassword = "changeit";
        String sslServerCertNickname = "tomcat";
        String keyPassword = "changeit";
        String caCertNickname = "caCert";
        Provider provider = new BouncyCastleProvider();
        if (Security.getProvider("BC") == null) {
            Security.addProvider(provider);
        }
        String issuerStr = "CN=xmldap Class 3 Extended Validation SSL CA, O=xmldap, L=San Francisco, ST=California, C=US";
        X509Name issuer = new X509Name(issuerStr);
        KeyPair caKeyPair = CertsAndKeys.generateKeyPair(provider);
        {
            PKCS12BagAttributeCarrier bagAttr = (PKCS12BagAttributeCarrier) caKeyPair.getPrivate();
            bagAttr.setBagAttribute(PKCSObjectIdentifiers.pkcs_9_at_friendlyName, new DERBMPString(caCertNickname));
            bagAttr.setBagAttribute(PKCSObjectIdentifiers.pkcs_9_at_localKeyId, new SubjectKeyIdentifierStructure(caKeyPair.getPublic()));
        }
        X509Certificate caCert = CertsAndKeys.generateCaCertificate(provider, "xmldap Class 3 Extended Validation SSL CA", caKeyPair, issuer);
        KeyPair kp = CertsAndKeys.generateKeyPair(provider);
        {
            PKCS12BagAttributeCarrier bagAttr = (PKCS12BagAttributeCarrier) kp.getPrivate();
            bagAttr.setBagAttribute(PKCSObjectIdentifiers.pkcs_9_at_friendlyName, new DERBMPString(sslServerCertNickname));
            bagAttr.setBagAttribute(PKCSObjectIdentifiers.pkcs_9_at_localKeyId, new SubjectKeyIdentifierStructure(kp.getPublic()));
        }
        String domain = "w4de3esy0069028.gdc-bln01.t-systems.com";
        String jurisdictionOfIncorporationCountryNameOidStr = "1.3.6.1.4.1.311.60.2.1.3";
        String jurisdictionOfIncorporationStateOrProvinceNameOidStr = "1.3.6.1.4.1.311.60.2.1.2";
        String jurisdictionOfIncorporationLocalityNameOidStr = "1.3.6.1.4.1.311.60.2.1.1";
        X509Name subject = new X509Name("CN=" + domain + ",OU=PD" + ",O=T-Systems" + ",L=Berlin" + ",street=Goslarer Ufer 35" + ",ST=Berlin" + ",postalCode=10589" + ",C=DE" + "," + jurisdictionOfIncorporationStateOrProvinceNameOidStr + "=Hessen" + "," + jurisdictionOfIncorporationCountryNameOidStr + "=DE" + ",SN=Handelsregister Amtsgericht Frankfurt am Main HRB 55933" + "," + jurisdictionOfIncorporationLocalityNameOidStr + "=Frankfurt am Main");
        X509Certificate cert = CertsAndKeys.generateSSLServerCertificate(provider, sslServerCertNickname, caKeyPair, caCert, kp, issuer, subject);
        try {
            cert.verify(caKeyPair.getPublic(), "BC");
            System.out.println("verified cert");
        } catch (Exception e) {
            System.out.println("could not verify cert");
        }
        String keystorePath = "";
        String caCertPath = "";
        String serverCertPath = "";
        String caCertKeyPath = "";
        String serverCertKeyPath = "";
        String certRequestPath = "";
        String tmpdir = System.getProperty("java.io.tmpdir");
        if (tmpdir != null) {
            keystorePath = tmpdir + "keystore.jks";
            caCertPath = tmpdir + "caCert.der";
            caCertKeyPath = tmpdir + "caCert-key.der";
            serverCertPath = tmpdir + domain + ".der";
            serverCertKeyPath = tmpdir + domain + "-key.der";
            certRequestPath = tmpdir + domain + ".csr";
        } else {
            File[] roots = File.listRoots();
            keystorePath = roots[0].getPath() + "keystore.jks";
            caCertPath = roots[0].getPath() + "caCert.der";
            caCertKeyPath = roots[0].getPath() + "caCert-key.der";
            serverCertPath = roots[0].getPath() + domain + ".der";
            serverCertKeyPath = roots[0].getPath() + domain + "-key.der";
            certRequestPath = roots[0].getPath() + domain + ".csr";
        }
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(null, storePassword.toCharArray());
        ks.setCertificateEntry(caCertNickname, caCert);
        Certificate[] chain = { cert, caCert };
        ks.setKeyEntry(sslServerCertNickname, kp.getPrivate(), keyPassword.toCharArray(), chain);
        File file = new File(keystorePath);
        file.createNewFile();
        FileOutputStream fos = new java.io.FileOutputStream(file);
        ks.store(fos, storePassword.toCharArray());
        fos.close();
        System.out.println("saved keystore to: " + keystorePath);
        file = new File(caCertPath);
        file.createNewFile();
        fos = new java.io.FileOutputStream(file);
        fos.write(caCert.getEncoded());
        fos.close();
        System.out.println("saved caCert to: " + caCertPath);
        file = new File(caCertKeyPath);
        file.createNewFile();
        fos = new java.io.FileOutputStream(file);
        fos.write(caKeyPair.getPrivate().getEncoded());
        fos.close();
        System.out.println("saved caCert private key to: " + caCertKeyPath);
        file = new File(serverCertKeyPath);
        file.createNewFile();
        fos = new java.io.FileOutputStream(file);
        fos.write(kp.getPrivate().getEncoded());
        fos.close();
        System.out.println("saved server private key to: " + serverCertKeyPath);
        file = new File(serverCertPath);
        file.createNewFile();
        fos = new java.io.FileOutputStream(file);
        fos.write(cert.getEncoded());
        fos.close();
        System.out.println("saved server certificate to: " + serverCertPath);
    }

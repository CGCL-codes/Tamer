    public UserZipFile makeCertificate() throws NullPointerException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException, InvalidKeyException {
        if (name == null) {
            throw new NullPointerException("Name is not set.");
        }
        if (passphrase == null) {
            throw new NullPointerException("Passphrase is not set.");
        }
        if (saveFile == null) {
            throw new RuntimeException("Save location is not set.");
        }
        SecurityUtil.lazyLoad();
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "BC");
        keyGen.initialize(1024);
        KeyPair keyPair = keyGen.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        Hashtable attrs = new Hashtable();
        attrs.put(X509Principal.CN, name);
        byte[] serno = new byte[8];
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(TimeUtil.getTrancheTimestamp());
        random.nextBytes(serno);
        BigInteger sn = new java.math.BigInteger(serno).abs();
        X509Principal principal = new X509Principal(attrs);
        X509V3CertificateGenerator gen = new X509V3CertificateGenerator();
        gen.setSerialNumber(sn);
        if (signerCertificate != null && signerPrivateKey != null) {
            gen.setIssuerDN((X509Principal) signerCertificate.getSubjectDN());
        } else {
            gen.setIssuerDN(principal);
        }
        gen.setNotBefore(startDate);
        gen.setNotAfter(new Date(startDate.getTime() + (validDays * Long.valueOf("86400000"))));
        gen.setSubjectDN(principal);
        gen.setSignatureAlgorithm("SHA1WITHRSA");
        gen.setPublicKey(publicKey);
        X509Certificate cert = null;
        if (signerCertificate != null && signerPrivateKey != null) {
            cert = gen.generateX509Certificate(getSignerPrivateKey());
        } else {
            cert = gen.generateX509Certificate(privateKey);
        }
        UserZipFile uzf = new UserZipFile(saveFile);
        uzf.setCertificate(cert);
        uzf.setPrivateKey(privateKey);
        uzf.setPassphrase(passphrase);
        uzf.saveTo(saveFile);
        return uzf;
    }

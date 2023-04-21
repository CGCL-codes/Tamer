    public static PGPPublicKeyRing generateKeyPair(String identity, char[] passPhrase, String privatePath, String publicPath) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyPairGenerator dsaKpg = KeyPairGenerator.getInstance("DSA", "BC");
        dsaKpg.initialize(1024);
        KeyPair dsaKp = dsaKpg.generateKeyPair();
        FileOutputStream secretOut = new FileOutputStream(privatePath);
        FileOutputStream publicOut = new FileOutputStream(publicPath);
        PGPKeyPair dsaKeyPair = new PGPKeyPair(PGPPublicKey.DSA, dsaKp, new Date());
        PGPKeyRingGenerator keyRingGen = new PGPKeyRingGenerator(PGPSignature.POSITIVE_CERTIFICATION, dsaKeyPair, identity, PGPEncryptedData.CAST5, passPhrase, true, null, null, new SecureRandom(), "BC");
        keyRingGen.generateSecretKeyRing().encode(secretOut);
        secretOut.close();
        PGPPublicKeyRing publicRing = keyRingGen.generatePublicKeyRing();
        publicRing.encode(publicOut);
        publicOut.close();
        return publicRing;
    }

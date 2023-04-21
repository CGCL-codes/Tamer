    public static PGPPublicKeyRing generateKeyPair(String userName, String identity, char[] passPhrase, int keyPairNumber, File profileDir) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyPairGenerator dsaKpg = KeyPairGenerator.getInstance("DSA", "BC");
        dsaKpg.initialize(1024);
        KeyPair dsaKp = dsaKpg.generateKeyPair();
        String path = profileDir.getPath() + File.separator + userName + File.separator + "keys" + File.separator;
        String keyNames = "key" + keyPairNumber;
        FileOutputStream secretOut = new FileOutputStream(path + keyNames + ".pri");
        FileOutputStream publicOut = new FileOutputStream(path + keyNames + ".pub");
        PGPKeyPair dsaKeyPair = new PGPKeyPair(PGPPublicKey.DSA, dsaKp, new Date());
        PGPKeyRingGenerator keyRingGen = new PGPKeyRingGenerator(PGPSignature.POSITIVE_CERTIFICATION, dsaKeyPair, identity, PGPEncryptedData.CAST5, passPhrase, true, null, null, new SecureRandom(), "BC");
        keyRingGen.generateSecretKeyRing().encode(secretOut);
        secretOut.close();
        PGPPublicKeyRing publicRing = keyRingGen.generatePublicKeyRing();
        publicRing.encode(publicOut);
        publicOut.close();
        return publicRing;
    }

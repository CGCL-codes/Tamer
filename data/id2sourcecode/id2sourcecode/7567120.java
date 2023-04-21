    public CreateSignatureOnKeyInfo(String[] args) throws Exception {
        if (args.length != 4) {
            System.out.println("\nPlease specify 4 command line parameter:");
            System.out.println("  (1) Path and filename for the IXSIL library init file");
            System.out.println("  (2) Path and filename for the XML file to sign\n");
            System.out.println("  (3) Name of the DPD in the Server Domain\n");
            System.out.println("  (4) Secret Key of the user. Used to decrypt the private key\n");
        }
        iaik.security.provider.IAIK.addAsJDK14Provider(true);
        URI tempURI = new URI("file", null, args[0], null, null);
        String cada = tempURI.toString();
        IXSILInit.init(new URI("file", null, args[0], null, null));
        RSAKeyPairGenerator rsaKeyPairGenerator = new RSAKeyPairGenerator();
        CryptoBag cryptoBag = rsaKeyPairGenerator.generateKeyPair();
        CryptoBag publicKey = cryptoBag.getCryptoBag(cryptoBag.V_KEY_PUBLIC);
        CryptoBag privateKey = cryptoBag.getCryptoBag(cryptoBag.V_KEY_PRIVATE);
        byte[] derEncodedSignerKey = privateKey.getEncoded();
        PKCS8EncodedKeySpec signerKeySpec = new PKCS8EncodedKeySpec(derEncodedSignerKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        signerKey_ = (RSAPrivateKey) keyFactory.generatePrivate(signerKeySpec);
        try {
            epki = new EncryptedPrivateKeyInfo(signerKey_);
            epki.encrypt(args[3], AlgorithmID.pbeWithMD5AndDES_CBC, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] derEncodedVerifierKey = publicKey.getEncoded();
        X509EncodedKeySpec verifierKeySpec = new X509EncodedKeySpec(derEncodedVerifierKey);
        keyFactory = KeyFactory.getInstance("RSA");
        verifierKey_ = (RSAPublicKey) keyFactory.generatePublic(verifierKeySpec);
    }

    @Test
    public void DSTest() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        keyPairGenerator.initialize(512);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        sign = SignUtils.sign(fileName, privateKey, signAlg);
        signVerify = SignUtils.verify(fileName, publicKey, signAlg, sign);
        Assert.assertTrue(signVerify);
    }

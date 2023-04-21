    @Test
    public void testPSS() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        SecureRandom random = new SecureRandom();
        keyPairGenerator.initialize(new RSAKeyGenParameterSpec(1024, RSAKeyGenParameterSpec.F4), random);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        Signature signature = Signature.getInstance("SHA256withRSA/PSS", "BC");
        byte[] data = "hello world".getBytes();
        signature.initSign(privateKey);
        signature.update(data);
        byte[] signatureValue = signature.sign();
        LOG.debug("signature size: " + signatureValue.length);
        LOG.debug("signature value: " + new String(Hex.encodeHex(signatureValue)));
        signature.initVerify(publicKey);
        signature.update(data);
        boolean result = signature.verify(signatureValue);
        assertTrue(result);
        signature.initSign(privateKey);
        signature.update(data);
        byte[] signatureValue2 = signature.sign();
        LOG.debug("signature size: " + signatureValue2.length);
        LOG.debug("signature value: " + new String(Hex.encodeHex(signatureValue2)));
        assertFalse(Arrays.equals(signatureValue, signatureValue2));
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256", "BC");
        byte[] digest = messageDigest.digest(data);
        signature = Signature.getInstance("RAWRSASSA-PSS", "BC");
        signature.setParameter(new PSSParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), 32, 1));
        signature.initVerify(publicKey);
        signature.update(digest);
        result = signature.verify(signatureValue);
        assertTrue(result);
    }

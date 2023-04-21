    public void xtest2() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(KeyPairType.RSA.getAlgorithm());
        KeyPair keyPair = generator.generateKeyPair();
        AsStringSigner signer = new AsStringSigner(new HexSigner(new KeyPairSigner(keyPair, SignatureType.SHA1_RSA)));
        FileInputStream inputStream = new FileInputStream("/home/lourival/.m2/repository/br/net/woodstock/rockframework/rockframework-core/1.2.2/rockframework-core-1.2.2.jar");
        byte[] bytes = IOUtils.toByteArray(inputStream);
        String sign = signer.signAsString(bytes);
        System.out.println(sign);
        boolean ok = signer.verifyAsString(bytes, sign);
        System.out.println(ok);
        inputStream.close();
    }

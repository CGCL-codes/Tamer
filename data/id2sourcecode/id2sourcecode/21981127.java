    @Test
    public void testSignAndVerifyFromBase64() throws Exception {
        String identity = "Alice";
        char[] pass = { 'p', 'a', 's', 's' };
        PGPPublicKeyRing ring1 = KeyUtil.generateKeyPairPlus(identity, pass);
        String key64 = KeyUtil.encodeBase64(ring1.getEncoded());
        File toSign = new File("test/util/test.txt");
        File signedFile = new File("test/util/test.asc");
        File keyFile = new File("key.pri");
        BufferedWriter out = new BufferedWriter(new FileWriter(toSign));
        out.write(key64 + NL + "" + NL);
        out.close();
        clearFileSigner.signFile(toSign, keyFile, signedFile, pass);
        BufferedReader input = new BufferedReader(new FileReader(signedFile));
        StringBuffer buff = new StringBuffer();
        String line;
        while ((line = input.readLine()) != null) {
            buff.append(line + NL);
        }
        buff.deleteCharAt(buff.length() - 1);
        String credential = buff.toString();
        PGPPublicKeyRing ring2 = new PGPPublicKeyRing(KeyUtil.decodeBase64(key64));
        assertTrue(CredentialSignatureVerifier.verifySignature(credential, ring2));
    }

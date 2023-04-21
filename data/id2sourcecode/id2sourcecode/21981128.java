    @Test
    public void testLotsOfStuff() throws Exception {
        String identity = "Alice";
        char[] pass = { 'p', 'a', 's', 's' };
        PGPPublicKeyRing ring1 = KeyUtil.generateKeyPairPlus(identity, pass);
        String outputA = KeyUtil.encodeBase64(ring1.getEncoded());
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        ring1.encode(bOut);
        byte[] bytes = bOut.toByteArray();
        String outputB = KeyUtil.encodeBase64(bytes);
        assertTrue(outputA.equals(outputB));
        File toSign = new File("test/util/test.txt");
        File signedFile = new File("test/util/test.asc");
        File keyFile = new File("key.pri");
        clearFileSigner.signFile(toSign, keyFile, signedFile, pass);
        BufferedReader input = new BufferedReader(new FileReader(signedFile));
        StringBuffer buff = new StringBuffer();
        String line;
        while ((line = input.readLine()) != null) {
            buff.append(line + NL);
        }
        buff.deleteCharAt(buff.length() - 1);
        String credential = buff.toString();
        assertTrue(CredentialSignatureVerifier.verifySignature(credential, ring1));
        PGPPublicKeyRing ring2 = new PGPPublicKeyRing(ring1.getEncoded());
        assertTrue(ring2.getPublicKey().getKeyID() == ring1.getPublicKey().getKeyID());
        PGPPublicKeyRing ring3 = new PGPPublicKeyRing(KeyUtil.decodeBase64(outputA));
        assertTrue(ring3.getPublicKey().getKeyID() == ring1.getPublicKey().getKeyID());
        assertTrue(CredentialSignatureVerifier.verifySignature(credential, ring3));
    }

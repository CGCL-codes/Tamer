    @Test
    public void createPSSSignature() throws Exception {
        this.messages = new Messages(Locale.GERMAN);
        PcscEid pcscEid = new PcscEid(new TestView(), this.messages);
        if (false == pcscEid.isEidPresent()) {
            LOG.debug("insert eID card");
            pcscEid.waitForEidPresent();
        }
        CardChannel cardChannel = pcscEid.getCardChannel();
        byte[] message = "hello world".getBytes();
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        byte[] digest = messageDigest.digest(message);
        try {
            CommandAPDU setApdu = new CommandAPDU(0x00, 0x22, 0x41, 0xB6, new byte[] { 0x04, (byte) 0x80, 0x10, (byte) 0x84, PcscEid.AUTHN_KEY_ID });
            ResponseAPDU responseAPDU = cardChannel.transmit(setApdu);
            assertEquals(0x9000, responseAPDU.getSW());
            pcscEid.verifyPin();
            CommandAPDU computeDigitalSignatureApdu = new CommandAPDU(0x00, 0x2A, 0x9E, 0x9A, digest);
            responseAPDU = cardChannel.transmit(computeDigitalSignatureApdu);
            assertEquals(0x9000, responseAPDU.getSW());
            byte[] signatureValue = responseAPDU.getData();
            List<X509Certificate> authnCertificateChain = pcscEid.getAuthnCertificateChain();
            Signature signature = Signature.getInstance("SHA1withRSA/PSS", "BC");
            signature.initVerify(authnCertificateChain.get(0).getPublicKey());
            signature.update(message);
            boolean result = signature.verify(signatureValue);
            assertTrue(result);
        } finally {
            pcscEid.close();
        }
    }

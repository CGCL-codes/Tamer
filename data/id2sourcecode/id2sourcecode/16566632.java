    @Test
    @QualityAssurance(firmware = Firmware.V012Z, approved = false)
    public void testNonRepSignPKCS1_SHA1() throws Exception {
        CardChannel cardChannel = this.pcscEid.getCardChannel();
        List<X509Certificate> signCertChain = this.pcscEid.getSignCertificateChain();
        CommandAPDU setApdu = new CommandAPDU(0x00, 0x22, 0x41, 0xB6, new byte[] { 0x04, (byte) 0x80, 0x02, (byte) 0x84, (byte) 0x83 });
        ResponseAPDU responseApdu = cardChannel.transmit(setApdu);
        assertEquals(0x9000, responseApdu.getSW());
        this.pcscEid.verifyPin();
        byte[] data = "My Testcase".getBytes();
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        byte[] digestValue = messageDigest.digest(data);
        CommandAPDU computeDigitalSignatureApdu = new CommandAPDU(0x00, 0x2A, 0x9E, 0x9A, digestValue);
        responseApdu = cardChannel.transmit(computeDigitalSignatureApdu);
        assertEquals(0x9000, responseApdu.getSW());
        byte[] signatureValue = responseApdu.getData();
        LOG.debug("signature value size: " + signatureValue.length);
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initVerify(signCertChain.get(0).getPublicKey());
        signature.update(data);
        boolean result = signature.verify(signatureValue);
        assertTrue(result);
    }

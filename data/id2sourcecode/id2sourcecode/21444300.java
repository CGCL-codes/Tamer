    @Test
    public void testCardSignature() throws Exception {
        PcscEid pcscEid = new PcscEid(new TestView(), this.messages);
        if (false == pcscEid.isEidPresent()) {
            LOG.debug("insert eID card");
            pcscEid.waitForEidPresent();
        }
        try {
            CardChannel cardChannel = pcscEid.getCardChannel();
            CommandAPDU setApdu = new CommandAPDU(0x00, 0x22, 0x41, 0xB6, new byte[] { 0x04, (byte) 0x80, 0x01, (byte) 0x84, (byte) 0x81 });
            ResponseAPDU responseApdu = cardChannel.transmit(setApdu);
            if (0x9000 != responseApdu.getSW()) {
                throw new RuntimeException("SELECT error");
            }
            byte[] message = "hello world".getBytes();
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            byte[] digestValue = messageDigest.digest(message);
            ByteArrayOutputStream digestInfo = new ByteArrayOutputStream();
            digestInfo.write(Constants.SHA1_DIGEST_INFO_PREFIX);
            digestInfo.write(digestValue);
            CommandAPDU computeDigitalSignatureApdu = new CommandAPDU(0x00, 0x2A, 0x9E, 0x9A, digestInfo.toByteArray());
            responseApdu = cardChannel.transmit(computeDigitalSignatureApdu);
            if (0x9000 != responseApdu.getSW()) {
                throw new RuntimeException("error CDS: " + Integer.toHexString(responseApdu.getSW()));
            }
        } finally {
            pcscEid.close();
        }
    }

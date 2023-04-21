    public boolean authenticate(byte[] authenticationKey, byte[] message, int messageOffset, int messageLength, ByteArrayWindow digest) {
        MessageDigest md = getDigestObject();
        byte[] newDigest;
        byte[] k_ipad = new byte[64];
        byte[] k_opad = new byte[64];
        for (int i = 0; i < MESSAGE_AUTHENTICATION_CODE_LENGTH; ++i) {
            digest.set(i, (byte) 0);
        }
        for (int i = 0; i < authenticationKey.length; ++i) {
            k_ipad[i] = (byte) (authenticationKey[i] ^ 0x36);
            k_opad[i] = (byte) (authenticationKey[i] ^ 0x5c);
        }
        for (int i = authenticationKey.length; i < 64; ++i) {
            k_ipad[i] = 0x36;
            k_opad[i] = 0x5c;
        }
        md.update(k_ipad);
        md.update(message, messageOffset, messageLength);
        newDigest = md.digest();
        md.reset();
        md.update(k_opad);
        md.update(newDigest);
        newDigest = md.digest();
        for (int i = 0; i < 12; ++i) {
            digest.set(i, newDigest[i]);
        }
        return true;
    }

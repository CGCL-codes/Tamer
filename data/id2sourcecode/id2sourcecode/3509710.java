    private static BigInteger makePKCS1(MessageDigest md, int modulus_byte_length, byte[] aid) throws SignatureException {
        byte[] theMD = md.digest();
        int mdl = theMD.length;
        byte[] r = new byte[modulus_byte_length];
        r[1] = 0x01;
        int aidl = aid.length;
        int padLen = modulus_byte_length - 3 - aidl - mdl;
        if (padLen < 0) throw new SignatureException("Signer's public key modulus too short.");
        for (int i = 0; i < padLen; ) r[2 + i++] = (byte) 0xFF;
        System.arraycopy(aid, 0, r, padLen + 3, aidl);
        System.arraycopy(theMD, 0, r, modulus_byte_length - mdl, mdl);
        return new BigInteger(r);
    }

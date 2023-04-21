    public static byte[] getSHA1_PKCS1Hash(byte[] data) throws Exception {
        int MODULUS_BYTE_LEN = 0x80;
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.reset();
        byte[] SHA1_ASN_DATA = { 0x30, 0x21, 0x30, 0x09, 0x06, 0x05, 0x2B, 0x0E, 0x03, 0x02, 0x1A, 0x05, 0x00, 0x04, 0x14 };
        int mdl = md.digest().length;
        int length = MODULUS_BYTE_LEN;
        int aidl = SHA1_ASN_DATA.length;
        int padLen = length - 3 - aidl - mdl;
        if (padLen < 0) throw new InvalidKeyException("Signer's key modulus too short.");
        md.update(data);
        return Util.toFixedLenByteArray(makePKCS1(md, MODULUS_BYTE_LEN, SHA1_ASN_DATA), MODULUS_BYTE_LEN);
    }

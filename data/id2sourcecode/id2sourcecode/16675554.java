    public static UUID nameUUIDFromBytes(byte abyte0[]) {
        MessageDigest messagedigest;
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nosuchalgorithmexception) {
            throw new InternalError("MD5 not supported");
        }
        byte abyte1[] = messagedigest.digest(abyte0);
        abyte1[6] &= 0xf;
        abyte1[6] |= 0x30;
        abyte1[8] &= 0x3f;
        abyte1[8] |= 0x80;
        return new UUID(abyte1);
    }

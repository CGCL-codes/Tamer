    public static byte[] getHash(byte[] data, int offset, int length) {
        byte[] tmp = new byte[20];
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(data, offset, length);
            md.digest(tmp, 0, tmp.length);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("SHA-1 algorithm for MessageDigest not supported");
        }
        return tmp;
    }

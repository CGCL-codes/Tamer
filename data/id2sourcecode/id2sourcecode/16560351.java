    private static final byte[] getChecksum(byte[] in, int offset, int len) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException nsae) {
            throw new RuntimeException("SHA1 message digest not available");
        }
        md.update(in, offset, len);
        byte[] cks = new byte[8];
        System.arraycopy(md.digest(), 0, cks, 0, cks.length);
        return cks;
    }

    public static byte[] getHash(byte[] data, int offset, int length) {
        MessageDigest SHA = null;
        try {
            SHA = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
        }
        SHA.reset();
        SHA.update(data, offset, length);
        return SHA.digest();
    }

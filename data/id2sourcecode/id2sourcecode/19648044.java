    public static byte[] simple(byte[] data, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest simp = MessageDigest.getInstance(algorithm);
        simp.update(data);
        return simp.digest();
    }

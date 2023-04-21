    private static byte[] getDigest(byte[] bytes, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance(algorithm);
        sha1.update(bytes);
        byte[] sha1Res = sha1.digest();
        return sha1Res;
    }

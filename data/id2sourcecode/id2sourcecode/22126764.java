    public static byte[] getMessageDigest(byte[] message, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest alg = MessageDigest.getInstance(algorithm);
        alg.update(message);
        return alg.digest();
    }

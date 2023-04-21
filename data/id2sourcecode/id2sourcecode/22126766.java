    public static boolean verifyMessageDigest(byte[] message, String algorithm, byte[] digest) throws NoSuchAlgorithmException {
        MessageDigest alg = MessageDigest.getInstance(algorithm);
        alg.update(message);
        return MessageDigest.isEqual(digest, alg.digest());
    }

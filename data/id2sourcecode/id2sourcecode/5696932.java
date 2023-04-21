    public static byte[] computeDigest(String hashAlgorithm, byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(hashAlgorithm);
        return md.digest(data);
    }

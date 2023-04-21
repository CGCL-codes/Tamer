    public static byte[] getMD5Digest(byte[] source) throws NoSuchAlgorithmException {
        return getMD5DigestAlgorithm().digest(source);
    }

    public static byte[] getSHADigest(byte source[]) throws NoSuchAlgorithmException {
        return getSHADigestAlgorithm().digest(source);
    }

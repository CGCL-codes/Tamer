    public static byte[] computeNodeDigest(String algorithm, byte[] left, byte[] right) throws NoSuchAlgorithmException {
        return CCNDigestHelper.digest(algorithm, left, right);
    }

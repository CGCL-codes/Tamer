    public static byte[] computeBlockDigest(String digestAlgorithm, byte[] content, int offset, int length) throws NoSuchAlgorithmException {
        return CCNDigestHelper.digest(digestAlgorithm, content, offset, length);
    }

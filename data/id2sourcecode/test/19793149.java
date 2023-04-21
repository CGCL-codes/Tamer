    public static byte[] computeBlockDigest(String digestAlgorithm, byte[] content) throws NoSuchAlgorithmException {
        return CCNDigestHelper.digest(digestAlgorithm, content);
    }

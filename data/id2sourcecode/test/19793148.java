    protected byte[] computeBlockDigest(int leafIndex, byte[] content, int offset, int length) throws NoSuchAlgorithmException {
        return CCNDigestHelper.digest(_digestAlgorithm, content, offset, length);
    }

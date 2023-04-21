    public static byte[] digest(String digestAlgorithm, byte[] content, int offset, int length) throws NoSuchAlgorithmException {
        CCNDigestHelper dh = new CCNDigestHelper(digestAlgorithm);
        dh.update(content, offset, length);
        return dh.digest();
    }

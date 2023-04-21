    public static byte[] digest(String digestAlgorithm, byte[] content, int offset, int length) throws NoSuchAlgorithmException {
        DigestHelper dh = new DigestHelper(digestAlgorithm);
        dh.update(content, offset, length);
        return dh.digest();
    }

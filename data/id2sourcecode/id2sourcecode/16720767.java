    public static byte[] digest(String digestAlgorithm, byte contents[][]) throws NoSuchAlgorithmException {
        CCNDigestHelper dh = new CCNDigestHelper(digestAlgorithm);
        for (int i = 0; i < contents.length; ++i) {
            if (null != contents[i]) dh.update(contents[i], 0, contents[i].length);
        }
        return dh.digest();
    }

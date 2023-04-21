    public static byte[] digest(String digestAlgorithm, byte[][] contents) throws NoSuchAlgorithmException {
        DigestHelper dh = new DigestHelper(digestAlgorithm);
        for (int i = 0; i < contents.length; ++i) {
            dh.update(contents[i], 0, contents[i].length);
        }
        return dh.digest();
    }

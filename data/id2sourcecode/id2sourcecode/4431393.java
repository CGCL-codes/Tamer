    public static Hash createHash(byte[] data, int length, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(data, 0, length);
        return new Hash(md.digest());
    }

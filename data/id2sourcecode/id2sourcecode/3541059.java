    public static byte[] digest(String algorithm, byte[] in) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(in);
        return md.digest();
    }

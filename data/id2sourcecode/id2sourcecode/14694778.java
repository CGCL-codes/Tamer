    public static String digest(byte b[], String alg) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(alg);
        md.update(b);
        return Base64.encodeBytes(md.digest());
    }

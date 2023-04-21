    public static String encryptPassword(String password, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest hash = MessageDigest.getInstance(algorithm);
        return toHexString(hash.digest(password.getBytes()));
    }

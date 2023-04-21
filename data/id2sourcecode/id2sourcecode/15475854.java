    public static byte[] sha256(byte[] text) throws NoSuchAlgorithmException {
        MessageDigest d = MessageDigest.getInstance("SHA-256");
        return d.digest(text);
    }

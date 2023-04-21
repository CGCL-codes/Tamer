    public static byte[] sha1(byte[] text) throws NoSuchAlgorithmException {
        MessageDigest d = MessageDigest.getInstance("SHA-1");
        return d.digest(text);
    }

    public static byte[] bytesSha256(String msg) {
        MessageDigest hash = null;
        try {
            hash = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        byte[] hashedBytes;
        try {
            hashedBytes = hash.digest(msg.getBytes(ENCODING));
            return hashedBytes;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

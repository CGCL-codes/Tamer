    public static String encryptHash(String in) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        in = in.trim().toUpperCase();
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        byte[] raw = digest.digest(in.getBytes("UTF-8"));
        return toHex(raw);
    }

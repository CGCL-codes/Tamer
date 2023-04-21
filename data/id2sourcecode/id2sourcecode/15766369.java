    public static String encodeSha256(String s) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest m = null;
        String result = null;
        m = MessageDigest.getInstance("SHA-256");
        m.reset();
        byte[] hash = m.digest(s.getBytes("UTF-8"));
        result = byteToBase64(hash);
        return result;
    }

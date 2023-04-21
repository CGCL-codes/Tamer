    public static String encode(byte[] src) {
        if (src == null) return null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return new String(HEX.encode(md.digest(src)));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

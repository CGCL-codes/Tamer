    public static String getHash(String s) {
        String hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            hash = ISOUtil.hexString(md.digest(s.getBytes())).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
        }
        return hash;
    }

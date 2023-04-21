    public static String getSHA1Text(String text) throws Exception {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        String hash = (new BASE64Encoder()).encode(sha1.digest(text.getBytes()));
        return hash;
    }

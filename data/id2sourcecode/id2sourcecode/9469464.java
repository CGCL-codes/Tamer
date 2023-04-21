    public static String md5(String message) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return hex(md.digest(message.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            return "hash" + message.hashCode();
        }
    }

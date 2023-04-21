    public static String sha1(String message) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            return hex(md.digest(message.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            return "hash" + message.hashCode();
        }
    }

    public static String sha1(String message) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            return hex(sha.digest(message.getBytes()));
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }

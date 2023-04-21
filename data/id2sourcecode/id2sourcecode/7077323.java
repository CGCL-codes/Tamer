    private static String H(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            return toHexString(digest.digest(data.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Failed to instantiate an MD5 algorithm", ex);
        }
    }

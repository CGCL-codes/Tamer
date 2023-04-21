    public static String cryptString(String original) {
        String copy = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] digest = md.digest(original.getBytes());
            for (int i = 0; i < digest.length; i++) {
                copy += Integer.toHexString(digest[i] & 0xFF);
            }
        } catch (NoSuchAlgorithmException nsae) {
            logError(nsae.getMessage());
        }
        return copy;
    }

    public static String digest(String planeText) {
        MessageDigest digest = createMessageDigest();
        byte[] b = planeText.getBytes();
        String hex = toHexString(digest.digest(b));
        return hex;
    }

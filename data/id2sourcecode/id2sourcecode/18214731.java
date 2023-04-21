    public static String getDigest(String s) {
        if (digest != null && s != null) {
            return digestToHexString(digest.digest(s.getBytes()));
        }
        return "";
    }

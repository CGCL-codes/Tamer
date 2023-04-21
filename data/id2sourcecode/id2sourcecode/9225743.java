    public static String rawChecksum(byte[] data) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException noMD5) {
            try {
                md = MessageDigest.getInstance("SHA");
            } catch (NoSuchAlgorithmException noSha) {
                throw new RuntimeException("No suitable MessageDigest found!");
            }
        }
        return new String(md.digest(data));
    }

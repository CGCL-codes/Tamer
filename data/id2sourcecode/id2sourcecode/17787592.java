    public static synchronized String hash(byte[] data) {
        if (digest == null) {
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException nsae) {
            }
        }
        digest.update(data);
        return encodeHex(digest.digest());
    }

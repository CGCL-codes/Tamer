    private static String getMD5Digest(byte[] bytes) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException x) {
            throw new RuntimeException("MD5 algorithm not supported.", x);
        }
        digest.reset();
        digest.update(bytes);
        String md5sum = bytesToString(digest.digest());
        return md5sum;
    }

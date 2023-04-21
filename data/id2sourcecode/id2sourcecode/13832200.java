    private static byte[] getMD5(byte[] data) {
        MessageDigest digest;
        try {
            digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(data);
            byte[] hash = digest.digest();
            return hash;
        } catch (NoSuchAlgorithmException e) {
            AQUtility.report(e);
        }
        return null;
    }

    private static String encryptPassword(String password) {
        try {
            MessageDigest hasher = MessageDigest.getInstance(SALT_HASH);
            byte[] digest = hasher.digest(password.getBytes(ENCODING));
            for (int i = 0; i < 9; i++) digest = hasher.digest(digest);
            return bytesToHexString(digest);
        } catch (Exception e) {
            return null;
        }
    }

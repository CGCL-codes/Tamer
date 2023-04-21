    public static byte[] digestPassword(String password) {
        byte[] b = password.getBytes();
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            synchronized (digest) {
                digest.reset();
                b = digest.digest(password.getBytes());
            }
        } catch (java.security.NoSuchAlgorithmException nsa) {
        }
        return b;
    }

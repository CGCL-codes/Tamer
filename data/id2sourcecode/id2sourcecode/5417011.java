    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hash = digest.digest(password.getBytes());
            return new String(encodeHex(hash));
        } catch (NoSuchAlgorithmException e) {
            LOG.error("WTF? SHA-1 not available", e);
            return password;
        }
    }

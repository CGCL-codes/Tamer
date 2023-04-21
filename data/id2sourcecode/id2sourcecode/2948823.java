    private static byte[] digest(String hashMethod, String password, byte[] salt) {
        byte[] passwordBytes = LdifUtils.utf8encode(password);
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(hashMethod);
        } catch (NoSuchAlgorithmException e1) {
            return null;
        }
        if (salt != null) {
            digest.update(passwordBytes);
            digest.update(salt);
            byte[] hashedPasswordBytes = digest.digest();
            return hashedPasswordBytes;
        } else {
            byte[] hashedPasswordBytes = digest.digest(passwordBytes);
            return hashedPasswordBytes;
        }
    }

    private static byte[] digest(LdapSecurityConstants algorithm, byte[] password, byte[] salt) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(algorithm.getName());
        } catch (NoSuchAlgorithmException e1) {
            return null;
        }
        if (salt != null) {
            digest.update(password);
            digest.update(salt);
            return digest.digest();
        } else {
            return digest.digest(password);
        }
    }

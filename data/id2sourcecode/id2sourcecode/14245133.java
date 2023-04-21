    public static String encodePassword(byte[] password) {
        byte[] salt = new byte[8];
        new SecureRandom().nextBytes(salt);
        byte[] passwordPlusSalt = new byte[password.length + salt.length];
        System.arraycopy(password, 0, passwordPlusSalt, 0, password.length);
        System.arraycopy(salt, 0, passwordPlusSalt, password.length, salt.length);
        MessageDigest sha1 = null;
        try {
            sha1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Could not get an SHA-1 MessageDigest instance to encode a password", ex);
        }
        byte[] passwordPlusSaltHash = sha1.digest(passwordPlusSalt);
        byte[] digestPlusSalt = new byte[passwordPlusSaltHash.length + salt.length];
        System.arraycopy(passwordPlusSaltHash, 0, digestPlusSalt, 0, passwordPlusSaltHash.length);
        System.arraycopy(salt, 0, digestPlusSalt, passwordPlusSaltHash.length, salt.length);
        return "{SSHA}" + new String(Base64.encodeBase64(digestPlusSalt));
    }

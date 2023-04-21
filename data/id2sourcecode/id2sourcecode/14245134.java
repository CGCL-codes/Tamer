    public static boolean checkPassword(byte[] password, String sshaPasswordString) {
        if (!sshaPasswordString.startsWith("{SSHA}")) return false;
        byte[] digestPlusSalt = Base64.decodeBase64(sshaPasswordString.substring(6).getBytes());
        byte[] salt = new byte[8];
        byte[] digestBytes = new byte[digestPlusSalt.length - 8];
        System.arraycopy(digestPlusSalt, 0, digestBytes, 0, digestBytes.length);
        System.arraycopy(digestPlusSalt, digestBytes.length, salt, 0, salt.length);
        byte[] passwordPlusSalt = new byte[password.length + salt.length];
        System.arraycopy(password, 0, passwordPlusSalt, 0, password.length);
        System.arraycopy(salt, 0, passwordPlusSalt, password.length, salt.length);
        MessageDigest sha1Digest = null;
        try {
            sha1Digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Could not get an SHA-1 MessageDigest instance to encode a password", ex);
        }
        byte[] passwordPlusSaltHash = sha1Digest.digest(passwordPlusSalt);
        return Arrays.equals(digestBytes, passwordPlusSaltHash);
    }

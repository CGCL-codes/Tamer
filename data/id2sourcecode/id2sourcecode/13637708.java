    public static String obfuscatePassword(String password, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] digestedBytes = digest.digest(password.getBytes());
            byte[] obfuscatedBytes = Base64Codec.encodeBase64(digestedBytes);
            return LEFT_DELIMITER + algorithm + RIGHT_DELIMITER + new String(obfuscatedBytes);
        } catch (NoSuchAlgorithmException x) {
            throw new SecurityException("Could not find digest algorithm " + algorithm);
        }
    }
